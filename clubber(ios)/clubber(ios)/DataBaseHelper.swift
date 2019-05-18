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
    
    
    //IST DAS HIER SINNVOLL? WIE VIELE CONTEXTS HAT EIN APP, WIE WIRD DIESER GESPEICHERT?
    
    //returs Context
    static func getContext () -> NSManagedObjectContext {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        return appDelegate.persistentContainer.viewContext
    }
    
    static func requestEventsFromDatabase(entity: String) -> [Event] {
        let context = getContext()
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: entity)
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
    
    static func requestClubsFromDatabase(entity: String) -> [Club] {
        let context = getContext()
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: entity)
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
    
    
    
    static func deleteOldEntries() {
        do {
            let context = getContext()
            let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Event")
            //fetch every entry, which is older than the date of yesterday
            fetchRequest.predicate = NSPredicate(format: "dte < %@", getDateOfYesterday() as CVarArg)
            let result = try context.fetch(fetchRequest)
            let resultData = result as! [Event]
            NSLog("%@ Events are dated before the date of yesterday", resultData.count)
            
            
            if (resultData.count > 0){
                //delete every fetched object
                for object in resultData {
                    context.delete(object)
                }
                try context.save()
                NSLog("%@ Events have been deleted", resultData.count)
            }
        } catch let error as NSError  {
            print("Error trying to delete old db entries \(error), \(error.userInfo)")
        }
    }
    
    static func deleteAll(){
        let context = getContext()
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
    static func requestHighestId(entity: String) -> Int {
        let context = getContext()
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: entity)
        request.entity = NSEntityDescription.entity(forEntityName: entity, in: context)
        request.resultType = NSFetchRequestResultType.dictionaryResultType
        
        //column to be selected
        let keypathExpression = NSExpression(forKeyPath: "id")
        //maximum id should be replied
        let maxExpression = NSExpression(forFunction: "max:", arguments: [keypathExpression])
        
        let key = "maxId"
        
        //key value pair
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
            NSLog("Highest %@ id is %i", entity, maxId!)
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
