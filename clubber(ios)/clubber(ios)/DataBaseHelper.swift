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
    
    //returns an array of requested data from the database, for example Events/Clubs
    static func requestDataFromDatabase(entity: String) -> [String]{
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
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
}
