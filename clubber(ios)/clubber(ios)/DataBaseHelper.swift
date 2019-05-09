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

    //returs Context
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
                    print((event as AnyObject).value(forKey: "id") as! Int)
                    arr.append((event as AnyObject).value(forKey: "name") as! String)
                    index += 1
                }
            }
        }
        catch{
            print(error)
        }
        return arr
    }
    
    
    static func deleteOldEntries(){
        
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        let date = dateFormatter.string(from: currentDate)
        let dateOfYesterdayDate = dateFormatter.date(from: date)
        
        let context = getContext()
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Events")
        print(dateOfYesterdayDate!)
        fetchRequest.predicate = NSPredicate(format: "dte < %@", dateOfYesterdayDate! as CVarArg)
        let result = try? context.fetch(fetchRequest)
        let resultData = result as! [Events]
        
        for object in resultData {
            context.delete(object)
        }
        
        do {
            try context.save()
            print("An old entry has been deleted")
        } catch let error as NSError  {
            print("Error trying to delete old db entries \(error), \(error.userInfo)")
        }
    }
    
    static func deleteAll(){
        let context = getContext()
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Events")
        
        let result = try? context.fetch(fetchRequest)
        let resultData = result as! [Events]
        
        for object in resultData {
            context.delete(object)
        }
        
        do {
            try context.save()
            print("An old entry has been deleted")
        } catch let error as NSError  {
            print("Error trying to delete old db entries \(error), \(error.userInfo)")
        }
    }
}
