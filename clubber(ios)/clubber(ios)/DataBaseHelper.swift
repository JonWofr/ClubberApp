//
//  DataBaseHelper.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.


import Foundation
import CoreData
import UIKit

class DataBaseHelper {
    
    static var newEventEntriesHaveBeenStored : Bool = false
    
    //IST DAS HIER SINNVOLL? WIE VIELE CONTEXTS HAT EIN APP, WIE WIRD DIESER GESPEICHERT
    //returns Context
    static func getContext () -> NSManagedObjectContext {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        return appDelegate.persistentContainer.viewContext
    }
    
    static func saveRequestedArraysInDatabase(jsonDataObj: HTTPHelper.JSONDataStruct, context: NSManagedObjectContext) {
        do{
            if (jsonDataObj.events.count == 0){
                NSLog("There are no new events which should be stored into the local db")
                newEventEntriesHaveBeenStored = false
            }
            //stores every event into the entity events in core object
            for event in jsonDataObj.events {
                //mirror saves every value of each column
                let eventMirror = Mirror(reflecting: event)
                //every mirror value will be gradually stored into the new row of the local db
                let newEventEntry = NSEntityDescription.insertNewObject(forEntityName: "Event", into: context)
                for children in eventMirror.children{
                    if (children.label! == "id"){
                        //ids should be stored as integer
                        let id = children.value as! String
                        newEventEntry.setValue(Int(id), forKey: children.label!)
                    }
                    else if (children.label! == "dte"){
                        //date has to be stored in date format
                        let dateFormatter = DateFormatter()
                        //the date datatype has an unchangeable format (i.e.) yyyy-MM-dd hh:mm:ss. dateFormat defines what the input String looks like
                        dateFormatter.dateFormat = "yyyy-MM-dd"
                        let dte = dateFormatter.date(from: children.value as! String)
                        newEventEntry.setValue(dte!, forKey: children.label!)
                    }
                    else{
                        //rest is stored as strings
                        newEventEntry.setValue(children.value as? String ?? "N/A", forKey: children.label!)
                    }
                    //the new entry will be saved
                    
                    NSLog("%@ of a new event is about to be stored into local db", children.value as? String ?? "nil")
                    //the new row will be saved
                    
                    try context.save()
                }
                NSLog("A new event entry has been made")
                newEventEntriesHaveBeenStored = true
            }
            if (jsonDataObj.clubs.count == 0){
                NSLog("There are no new clubs which should be stored into the local db")
            }
            //stores every club into the entity events in core object
            for club in jsonDataObj.clubs {
                //mirror saves every value of each column
                let clubMirror = Mirror(reflecting: club)
                //every mirror value will be gradually stored into the new row of the local db
                let newClubEntry = NSEntityDescription.insertNewObject(forEntityName: "Club", into: context)
                for children in clubMirror.children{
                    if (children.label! == "id"){
                        //ids should be stored as integer
                        let id = children.value as! String
                        newClubEntry.setValue(Int(id), forKey: children.label!)
                    }
                    else{
                        //rest is stored as strings
                        newClubEntry.setValue(children.value as? String ?? "N/A", forKey: children.label!)
                    }
                    
                    NSLog("%@ of a new club is about to be stored into local db", children.value as? String ?? "nil")
                    //the new entry will be saved
                    try context.save()
                }
                NSLog("A new club entry has been made")
            }
        }catch{
            NSLog("Error saving data into local db. Error:%@", error.localizedDescription)
        }
    }
    
    static func requestEventsFromDatabase(context: NSManagedObjectContext) -> [Event] {
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "Event")
        let sortDate = NSSortDescriptor(key: "dte", ascending: true)
        let sortTime = NSSortDescriptor(key: "srttime", ascending: true)
        let sortDescriptors = [sortDate, sortTime]
        request.sortDescriptors = sortDescriptors
        request.returnsObjectsAsFaults = false
        var results : [Event] = []
        do{
            results = try context.fetch(request) as! [Event]
        }
        catch{
            print(error)
        }
        return results
    }
    
    static func requestClubsFromDatabase(context: NSManagedObjectContext) -> [Club] {
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "Club")
        request.returnsObjectsAsFaults = false
        var results : [Club] = []
        do{
            results = try context.fetch(request) as! [Club]
        }
        catch{
            print(error)
        }
        return results
    }
    
    
    
    static func deleteOldEntries(context: NSManagedObjectContext) {
        do {
            let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Event")
            //fetch every entry, which is older than the date of yesterday
            fetchRequest.predicate = NSPredicate(format: "dte < %@", getDateOfYesterday() as CVarArg)
            let result = try context.fetch(fetchRequest)
            let resultData = result as! [Event]
            NSLog("%d Events are dated before the date of yesterday", resultData.count)
            
            
            if (resultData.count > 0){
                //delete every fetched object
                for object in resultData {
                    context.delete(object)
                }
                try context.save()
                NSLog("%d Events have been deleted", resultData.count)
            }
        } catch let error as NSError  {
            print("Error trying to delete old db entries \(error), \(error.userInfo)")
        }
    }
    
    static func deleteAll(context: NSManagedObjectContext){
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Event")
        
        let result = try? context.fetch(fetchRequest)
        let resultData = result as! [Event]
        
        for object in resultData {
            context.delete(object)
        }
        
        do {
            try context.save()
            print("An old entry has been deleted")
        } catch let error as NSError  {
            NSLog("An error occured trying to delete old db entries. Error code %@", error)
        }
    }
    
    //function to directly request the highest id stored in the coreData
    static func requestHighestId(entity: String, context: NSManagedObjectContext) -> Int {
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: entity)
        request.entity = NSEntityDescription.entity(forEntityName: entity, in: context)
        //this ensures the result to be key - value
        request.resultType = NSFetchRequestResultType.dictionaryResultType
        
        //column to be selected
        let keypathExpression = NSExpression(forKeyPath: "id")
        //maximum id should be replied
        let maxExpression = NSExpression(forFunction: "max:", arguments: [keypathExpression])
        
        let key = "maxId"
        
        let expressionDescription = NSExpressionDescription()
        expressionDescription.name = key
        expressionDescription.expression = maxExpression
        expressionDescription.expressionResultType = .integer64AttributeType
        
        request.propertiesToFetch = [expressionDescription]
        
        var maxId: Int? = 0
        
        
        do{
            //request with the specified key value pair
            if let result = try context.fetch(request) as? [[String: Int]], let dict = result.first {
                maxId = dict[key]
            }
            NSLog("Highest %@ id is %d", entity, maxId!)
            return maxId!
        }
        catch{
            NSLog("The local db has not been created yet. There is no highest id of clubs, or events.")
            return 0
        }
    }
    
    private static func getDateOfYesterday () -> Date {
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        let date = dateFormatter.string(from: currentDate)
        NSLog("Todays date is %@", date)
        let dateOfYesterday = dateFormatter.date(from: date)
        return dateOfYesterday!
    }
  
    
    
}
