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
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
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
        initStubs()
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        flushData()
    }
    
    
    func initStubs() {
        
        let event = HTTPHelper.EventStruct(id : "1", dte : "2020-01-01", name : "For unit testing", club : "Schräglage", srttime : "20:00", genre : "house", btn : "https://google.de")
        let events: [HTTPHelper.EventStruct]? = [event]
        
        let clubs : [HTTPHelper.ClubStruct]? = []
        
        let json = HTTPHelper.JSONDataStruct(events: events, clubs: clubs)
        
        DataBaseHelper.saveRequestedArraysInDatabase(jsonDataObj: json, context: mockPersistantContainer.viewContext)
    }
    
    func flushData() {
        
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Event")
        let objs = try! mockPersistantContainer.viewContext.fetch(fetchRequest)
        for case let obj as NSManagedObject in objs {
            mockPersistantContainer.viewContext.delete(obj)
        }
        try! mockPersistantContainer.viewContext.save()
        
    }
    
    func testRequestEventsFromDatabase() {
        let events = DataBaseHelper.requestEventsFromDatabase(context: mockPersistantContainer.viewContext)
        XCTAssertEqual(events.count, 1)
    }
}
