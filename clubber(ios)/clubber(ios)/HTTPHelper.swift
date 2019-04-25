//  HTTPHelper.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.


import UIKit
import Foundation
import CoreData
import os.log

class HTTPHelper{
    
    let log = OSLog(subsystem: Bundle.main.bundleIdentifier!, category: "network")

    
    //JSONData struct that stores Event and Club object Arrays
    struct JSONData : Decodable {
        var events :[Event]!
        var clubs : [Club]!
    }
    
    struct Event : Decodable {
        var id : String!
        var dte : String!
        var name : String!
        var club : String!
        var srttime : String!
        var genre : String!
        var btn : String!
    }
    
    struct Club : Decodable {
        var id : String!
        var name : String!
        var adrs : String!
        var tel : String!
        var web : String!
    }
    
    
    static func requestResponseServer(){
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        
        
        func requestHighestId(entity: String) -> Int {
            let request = NSFetchRequest<NSFetchRequestResult>(entityName: entity)
            request.entity = NSEntityDescription.entity(forEntityName: entity, in: context)
            request.resultType = NSFetchRequestResultType.dictionaryResultType
            
            let keypathExpression = NSExpression(forKeyPath: "id")
            let maxExpression = NSExpression(forFunction: "max:", arguments: [keypathExpression])
            
            let key = "maxId"
            
            let expressionDescription = NSExpressionDescription()
            expressionDescription.name = key
            expressionDescription.expression = maxExpression
            expressionDescription.expressionResultType = .integer64AttributeType
            
            request.propertiesToFetch = [expressionDescription]
            
            var maxId: Int? = 0
            
            
            do{
                if let result = try context.fetch(request) as? [[String: Int]], let dict = result.first {
                    maxId = dict[key]
                }
                return maxId!
            }
            catch{
                print(error)
                return 0
            }
        }
        
        let highestEventId = requestHighestId(entity: "Events")
        let highestClubId = requestHighestId(entity: "Clubs")
        
       
        print(highestClubId)
        
        let url = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=\(highestEventId)&&idClub=\(highestClubId)"
        let urlObj = URL(string: url)
        
        //starts Thread
        URLSession.shared.dataTask(with: urlObj!) {(data, response, error) in
            do {
                //jsonData is object of JSONData struct which contains an Object Array of Event- and Club-struct
                let jsonData = try JSONDecoder().decode(JSONData.self, from: data!)
                
                saveArraysInDatabase(jsonDataObj: jsonData)
                
                
            }catch{
                print(error)
            }
            
            }.resume()
        
        
        func saveArraysInDatabase(jsonDataObj: JSONData) {
            do{
                for event in jsonDataObj.events {
                    let eventMirror = Mirror(reflecting: event)
                    let newEventEntry = NSEntityDescription.insertNewObject(forEntityName: "Events", into: context)
                    for children in eventMirror.children{
                        if (children.label! == "id"){
                            let id = children.value as! String
                            newEventEntry.setValue(Int(id), forKey: children.label!)
                        }else{
                            newEventEntry.setValue(children.value as? String ?? "N/A", forKey: children.label!)
                        }
                        print("value: \(children.value as! String) forKey: \(children.label!)")
                        try context.save()
                        print("saved")
                    }
                }
                for club in jsonDataObj.clubs {
                    let clubMirror = Mirror(reflecting: club)
                    let newClubEntry = NSEntityDescription.insertNewObject(forEntityName: "Clubs", into: context)
                    for children in clubMirror.children{
                        if (children.label! == "id"){
                            let id = children.value as! String
                            newClubEntry.setValue(Int(id), forKey: children.label!)
                        }else{
                            newClubEntry.setValue(children.value as? String ?? "N/A", forKey: children.label!)
                        }
                        print("value: \(children.value as! String) forKey: \(children.label!)")
                        try context.save()
                        print("saved")
                    }
                }
            }catch{
                print("opps")
                print(error)
            }
        }
    }
}








/*
 func requestDataFromDatabase(entity: String){
 let request = NSFetchRequest<NSFetchRequestResult>(entityName: entity)
 request.returnsObjectsAsFaults = false
 do{
 let results = try context.fetch(request)
 if(results.count > 0){
 
 for event in results {
 print((event as AnyObject).value(forKey: "id") as! Int)
 }
 }
 }
 catch{
 print(error)
 }
 }
 */







