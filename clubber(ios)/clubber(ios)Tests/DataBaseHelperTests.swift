//
//  clubber_ios_Tests.swift
//  clubber(ios)Tests
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import XCTest
import CoreData
@testable import clubber_ios_

class DataBaseHelperTests: XCTestCase {
    
    var databaseHelper: DataBaseHelper!
    var mockPersistantContainer: NSPersistentContainer!
    var managedObjectModel: NSManagedObjectModel!
    
    override func setUp() {
        // This method is called before the invocation of each test method in the class. We are creating a designated container for testing, which stores db entries on the RAM instead on persistent storage
        
        managedObjectModel = {
            let managedObjectModel = NSManagedObjectModel.mergedModel(from: [Bundle(for: type(of: self))] )!
            return managedObjectModel
        }()
        
        mockPersistantContainer = {
            
            let container = NSPersistentContainer(name: "PersistentTodoList", managedObjectModel: self.managedObjectModel)
            let description = NSPersistentStoreDescription()
            description.type = NSInMemoryStoreType
            description.shouldAddStoreAsynchronously = false // Make it simpler in test env
            
            container.persistentStoreDescriptions = [description]
            container.loadPersistentStores { (description, error) in
                // Check if the data store is in memory
                precondition( description.type == NSInMemoryStoreType )
                
                // Check if creating container wrong
                if let error = error {
                    fatalError("Create an in-mem coordinator failed \(error)")
                }
            }
            return container
        }()
        //makes a two event entries and one club entry into the created db
        initStubs()
    }
    
    override func tearDown() {
        // This method is called after the invocation of each test method in the class. Deletes all data currently present in the db stored on the RAM
        flushData()
    }
    
    
    func initStubs() {
        var newEventEntry = NSEntityDescription.insertNewObject(forEntityName: "Event", into: mockPersistantContainer.viewContext)
        //date has to be stored in date format
        let dateFormatter = DateFormatter()
        //the date datatype has an unchangeable format (i.e.) yyyy-MM-dd hh:mm:ss. dateFormat defines what the input String looks like
        dateFormatter.dateFormat = "yyyy-MM-dd"
        var dte = dateFormatter.date(from: "2019-12-12")
        
        newEventEntry.setValue(1, forKey: "id")
        newEventEntry.setValue(dte, forKey: "dte")
        newEventEntry.setValue("event name 1", forKey: "name")
        newEventEntry.setValue("event club 1", forKey: "club")
        newEventEntry.setValue("event srttime 1", forKey: "srttime")
        newEventEntry.setValue("event genre 1", forKey: "genre")
        newEventEntry.setValue("event btn 1", forKey: "btn")
        
        newEventEntry = NSEntityDescription.insertNewObject(forEntityName: "Event", into: mockPersistantContainer.viewContext)
        
        dte = dateFormatter.date(from: "2019-01-01")

        newEventEntry.setValue(2, forKey: "id")
        newEventEntry.setValue(dte, forKey: "dte")
        newEventEntry.setValue("event name 2", forKey: "name")
        newEventEntry.setValue("event club 2", forKey: "club")
        newEventEntry.setValue("event srttime 2", forKey: "srttime")
        newEventEntry.setValue("event genre 2", forKey: "genre")
        newEventEntry.setValue("event btn 2", forKey: "btn")
        
        let newClubEntry = NSEntityDescription.insertNewObject(forEntityName: "Club", into: mockPersistantContainer.viewContext)
        
        newClubEntry.setValue(1, forKey: "id")
        newClubEntry.setValue("club name 1", forKey: "name")
        newClubEntry.setValue("club adrs 1", forKey: "adrs")
        newClubEntry.setValue("club tel 1", forKey: "tel")
        newClubEntry.setValue("club web 1", forKey: "web")

        try? mockPersistantContainer.viewContext.save()
    }
    
    func flushData() {
        var fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Event")
        var objs = try! mockPersistantContainer.viewContext.fetch(fetchRequest)
        for case let obj as NSManagedObject in objs {
            mockPersistantContainer.viewContext.delete(obj)
        }
        fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Club")
        objs = try! mockPersistantContainer.viewContext.fetch(fetchRequest)
        for case let obj as NSManagedObject in objs {
            mockPersistantContainer.viewContext.delete(obj)
        }
        try! mockPersistantContainer.viewContext.save()
    }
    
    func testSaveRequestedArraysInDatabase(){
        //Tests if the number of event entries is increased by one if we add a new one to the db
        
        let event = HTTPHelper.EventStruct(id : "3", dte : "2020-01-01", name : "event name 3", club : "event club 3", srttime : "event srttime 3", genre : "event genre 3", btn : "event btn 3")
        var events: [HTTPHelper.EventStruct]? = [event]
        
        let clubs : [HTTPHelper.ClubStruct]? = []
        
        var json = HTTPHelper.JSONDataStruct(events: events, clubs: clubs)
        
        DataBaseHelper.saveRequestedArraysInDatabase(jsonDataObj: json, context: mockPersistantContainer.viewContext)
        
        var allEvents = DataBaseHelper.requestEventsFromDatabase(context: mockPersistantContainer.viewContext)

        XCTAssertEqual(allEvents.count, 3)
        
        //Tests if the number of entries is not increased if we pass an empty json object
        
        events = []
    
        json = HTTPHelper.JSONDataStruct(events: events, clubs: clubs)
        
        DataBaseHelper.saveRequestedArraysInDatabase(jsonDataObj: json, context: mockPersistantContainer.viewContext)
        
        allEvents = DataBaseHelper.requestEventsFromDatabase(context: mockPersistantContainer.viewContext)
        
        XCTAssertEqual(allEvents.count, 3)
    }
    
    func testRequestEventsFromDatabase() {
        //Tests if the two entries made in setUp() have been stored correctly and now present in the db
        let events = DataBaseHelper.requestEventsFromDatabase(context: mockPersistantContainer.viewContext)
        XCTAssertEqual(events.count, 2)
    }
    
    /*
    func testRequestHighestId(){
        HTTPHelper.dispatchGroup.notify(queue: .main){
            let highestEventId = DataBaseHelper.requestHighestId(entity: "Event", context: self.mockPersistantContainer.viewContext)
            XCTAssertEqual(highestEventId, 2)
            
            let highestClubId = DataBaseHelper.requestHighestId(entity: "Club", context: self.mockPersistantContainer.viewContext)
            XCTAssertEqual(highestClubId, 1)
        }
    }
 */
    
    func testDeleteOldEntries(){
        //one entry is outdated. Checks if the number of total entries is subtracted by one after the method is called
        DataBaseHelper.deleteOldEntries(context: mockPersistantContainer.viewContext)
        let events = DataBaseHelper.requestEventsFromDatabase(context: mockPersistantContainer.viewContext)
        XCTAssertEqual(events.count, 1)
    }
}
