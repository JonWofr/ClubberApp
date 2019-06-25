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
    static var requestResponseServerIsRunning : Bool = false
    static let dispatchGroup = DispatchGroup()
    
    //checks if device got internetAccess
    //is updated when network state has changed (turned off/on)
    static func startConnectionListener(){
        let monitor = NWPathMonitor()
        
        //sets the static variable hasNetworkAccess true/false to check in different context the connection
        monitor.pathUpdateHandler = { path in
            if path.status == .satisfied {
                NSLog("Connection has been established")
                hasNetworkAccess = true
            } else {
                NSLog("No connection available...")
                hasNetworkAccess = false
            }
        }

        
        let queue = DispatchQueue(label: "Monitor")
        monitor.start(queue: queue)
    }
    
    
    
    //JSONData struct that stores Event and Club object Arrays
    public struct JSONDataStruct : Decodable {
        var events :[Event]!
        var clubs : [Club]!
    }
    
    //comparabe with an event object
    public struct Event : Decodable {
        var id : String!
        var dte : String!
        var name : String!
        var club : String!
        var srttime : String!
        var genre : String!
        var btn : String!
    }
    
    //comparabe with a club object
    public struct Club : Decodable {
        var id : String!
        var name : String!
        var adrs : String!
        var tel : String!
        var web : String!
    }
    
    //For requesting and receiving a json file from our webserver
    static func requestResponseServer(){
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let dataBaseHelper = appDelegate.dataBaseHelper!
        
        requestResponseServerIsRunning = true
        
        //We implemented this because if there is nothing to download and the user uses the refreshfunction, he could think the application didn't properly refresh
        DispatchQueue.global(qos: .background).async {
            dispatchGroup.enter()
            usleep(1500000)
            dispatchGroup.leave()
        }
        
        dataBaseHelper.deleteOldEntries()
        
        let highestEventId = dataBaseHelper.requestHighestId(entity: "Event")
        let highestClubId = dataBaseHelper.requestHighestId(entity: "Club")
        
        let url = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=\(highestEventId)&&idClub=\(highestClubId)"
        
        NSLog("Requesting data from %@", url)

        let urlObj = URL(string: url)
        
        //starts request - reply to the server with url depending on the highest stored id in the local db
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        dispatchGroup.enter()
        URLSession.shared.dataTask(with: urlObj!) {(data, response, error) in
            do {
                //jsonData is object of JSONData struct which contains an Object Array of Event- and Club-struct
                let jsonData = try JSONDecoder().decode(JSONDataStruct.self, from: data!)
                NSLog("JSONData object has been created")
                
                dataBaseHelper.events = jsonData.events
                dataBaseHelper.clubs = jsonData.clubs
                
                NSLog("Data has been passed to DataBaseHelper")
                
                dataBaseHelper.save()
            }catch{
                //localizedDescription is needed to convert NSError into String
                NSLog("Requesting data from given URL has been unsuccessful. Error: %@", error.localizedDescription)
            }
            requestResponseServerIsRunning = false
            dispatchGroup.leave()
            }.resume()
    }
}
