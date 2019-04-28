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
    
    //JSONData struct that stores Event and Club object Arrays
    struct JSONData : Decodable {
        var events :[Event]!
        var clubs : [Club]!
    }
    
    //comparabe with an event object
    struct Event : Decodable {
        var id : String!
        var dte : String!
        var name : String!
        var club : String!
        var srttime : String!
        var genre : String!
        var btn : String!
    }
    
    //comparabe with a club object
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
        
        
        //function to directly request the highest id stored in the coreData
        func requestHighestId(entity: String) -> Int {
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
                return maxId!
            }
            catch{
                print(error)
                return 0
            }
        }
        
        let highestEventId = requestHighestId(entity: "Events")
        let highestClubId = requestHighestId(entity: "Clubs")
        
        
        let url = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=\(highestEventId)&&idClub=\(highestClubId)"
        
        let urlObj = URL(string: url)
        
        //starts request - reply to the server with url depending on the highest stored id in the local db
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
                //stores every event into the entity events in core object
                for event in jsonDataObj.events {
                    //mirror saves every value of each column
                    let eventMirror = Mirror(reflecting: event)
                    //every mirror value will be gradually stored into the new row of the local db
                    let newEventEntry = NSEntityDescription.insertNewObject(forEntityName: "Events", into: context)
                    for children in eventMirror.children{
                        if (children.label! == "id"){
                            //ids should be stored as integer
                            let id = children.value as! String
                            newEventEntry.setValue(Int(id), forKey: children.label!)
                        }else{
                            //rest is stored as strings
                            newEventEntry.setValue(children.value as? String ?? "N/A", forKey: children.label!)
                        }
                        print("value: \(children.value as! String) forKey: \(children.label!)")
                        //the new row will be saved
                        try context.save()
                        print("saved")
                    }
                }
                //stores every club into the entity events in core object
                for club in jsonDataObj.clubs {
                    //mirror saves every value of each column
                    let clubMirror = Mirror(reflecting: club)
                    //every mirror value will be gradually stored into the new row of the local db
                    let newClubEntry = NSEntityDescription.insertNewObject(forEntityName: "Clubs", into: context)
                    for children in clubMirror.children{
                        if (children.label! == "id"){
                            //ids should be stored as integer
                            let id = children.value as! String
                            newClubEntry.setValue(Int(id), forKey: children.label!)
                        }else{
                            //rest is stored as strings
                            newClubEntry.setValue(children.value as? String ?? "N/A", forKey: children.label!)
                        }
                        print("value: \(children.value as! String) forKey: \(children.label!)")
                        //the new row will be saved
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
