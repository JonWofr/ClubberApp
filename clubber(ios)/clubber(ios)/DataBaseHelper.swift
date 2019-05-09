//
//  DataBaseHelper.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import Foundation
import CoreData
import UIKit

class DataBaseHelper {
    
    static func getContext () -> NSManagedObjectContext {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        return appDelegate.persistentContainer.viewContext
    }
    
    //returns an array of requested data from the database, for example Events/Clubs
    static func requestDataFromDatabase(entity: String) -> [String]{
        let context = getContext()
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: entity)
        request.returnsObjectsAsFaults = false
        var arr : [String] = []
        do{
            let results = try context.fetch(request)
            if(results.count > 0){
                var index = 0
                for event in results {
                    arr.append( (event as AnyObject).value(forKey: "name") as! String)
                    index += 1
                }
            }
        }
        catch{
            print(error)
        }
        return arr
    }
    
    
    static func deleteOldEntries() {
        
        do {
        let context = getContext()
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Events")
        //fetch every entry, which is older than the date of yesterday
        fetchRequest.predicate = NSPredicate(format: "dte < %@", getDateOfYesterday() as CVarArg)
        let result = try context.fetch(fetchRequest)
        let resultData = result as! [Events]
            
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
            NSLog("An error occured trying to delete old db entries. Error code %@", error)
        }
    }
    
    private static func getDateOfYesterday () -> Date {
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        let date = dateFormatter.string(from: currentDate)
        NSLog("Todays date is %@", date)
        let dateOfYesterdayDate = dateFormatter.date(from: date)
        return dateOfYesterdayDate!
    }
}
