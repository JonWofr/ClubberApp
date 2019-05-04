//  HTTPHelper.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.


import UIKit
import Foundation
import CoreData
import Network

class HTTPHelper{
    
    static var hasNetworkAccess : Bool = false
    private static var automaticDownloadHasBeenSuccessful : Bool = false
    
    //checks if device got internetAccess
    //is updated when network state has changed (turned off/on)
    static func startConnectionListener(){
        let monitor = NWPathMonitor()
        
        //sets the static variable hasNetworkAccess true/false to check in different context the connection
        monitor.pathUpdateHandler = { path in
            if path.status == .satisfied {
                print("We are connected!")
                hasNetworkAccess = true
            } else {
                print("No connection available...")
                hasNetworkAccess = false
            }
            
            //If we don't have network access at the beginning, but have internet while runtime, the app will start to request our webserver. if it was successful, it will set the automaticDownloadHasBeenSuccesful variable to true and we won't call the methode ever again while runtime
            
            if !automaticDownloadHasBeenSuccessful && hasNetworkAccess {
                requestResponseServer()
            }
        }
        let queue = DispatchQueue(label: "Monitor")
        monitor.start(queue: queue)
    }
    
    //JSONData struct that stores Event and Club object Arrays
    private struct JSONData : Decodable {
        var events :[Event]!
        var clubs : [Club]!
    }
    
    //comparabe with an event object
    private struct Event : Decodable {
        var id : String!
        var dte : String!
        var name : String!
        var club : String!
        var srttime : String!
        var genre : String!
        var btn : String!
    }
    
    //comparabe with a club object
    private struct Club : Decodable {
        var id : String!
        var name : String!
        var adrs : String!
        var tel : String!
        var web : String!
    }
    
    //For requesting and receiving a json file from our webserver
    static func requestResponseServer(){
        
        //----------------------
        //ToDo: sollte in MainThread aufgerufen werden, in Vorlesung fragen warum
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        //----------------------
        
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
                NSLog("Highest %@ id is %i", entity, maxId!)
                return maxId!
            }
            catch{
                NSLog("The local db has not been created yet. There is no highest id of clubs, or events.")
                return 0
            }
        }
        
        let highestEventId = requestHighestId(entity: "Events")
        let highestClubId = requestHighestId(entity: "Clubs")

        let url = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=\(highestEventId)&&idClub=\(highestClubId)"
        
        NSLog("Requesting data from %@", url)

        let urlObj = URL(string: url)
        
        //starts request - reply to the server with url depending on the highest stored id in the local db
        URLSession.shared.dataTask(with: urlObj!) {(data, response, error) in
            do {
                //jsonData is object of JSONData struct which contains an Object Array of Event- and Club-struct
                let jsonData = try JSONDecoder().decode(JSONData.self, from: data!)
                NSLog("JSONData object has been created")
                saveArraysInDatabase(jsonDataObj: jsonData)
                
                //if this is the initial/automatic update of the database it will be set true, so the database isn't
                //updating it self again, only the user is able to
                if(!automaticDownloadHasBeenSuccessful){
                    automaticDownloadHasBeenSuccessful = true
                }
                
            }catch{
                //localizedDescription is needed to convert NSError into String
                NSLog("Requesting data from given URL has been unsuccessful. Error: %@", error.localizedDescription)
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
                            //rest is stored as stringsy
                            newEventEntry.setValue(children.value as? String ?? "N/A", forKey: children.label!)
                        }
                        print(children.value)
                        //the new row will be saved
                        try context.save()
                    }
                    NSLog("A new event entry has been made")
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
                        }
                        else{
                            //rest is stored as strings
                            newClubEntry.setValue(children.value as? String ?? "N/A", forKey: children.label!)
                            print(children.value)
                        }
                        //the new row will be saved
                        try context.save()
                    }
                    NSLog("A new club entry has been made")
                }
            }catch{
                NSLog("Error saving data into local db. Error:%@", error.localizedDescription)
            }
        }
    }
    
}
