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
        var events :[EventStruct]!
        var clubs : [ClubStruct]!
    }
    
    //comparabe with an event object
    public struct EventStruct : Decodable {
        var id : String!
        var dte : String!
        var name : String!
        var club : String!
        var srttime : String!
        var genre : String!
        var btn : String!
    }
    
    //comparabe with a club object
    public struct ClubStruct : Decodable {
        var id : String!
        var name : String!
        var adrs : String!
        var tel : String!
        var web : String!
    }
    
    static var json : String! = ""
    static let dispatchGroup = DispatchGroup()

    
    //For requesting and receiving a json file from our webserver
    static func requestResponseServer(){
        requestResponseServerIsRunning = true
        let context = DataBaseHelper.getContext()
        
        //We implemented this because if there is nothing to download and the user uses the refreshfunction, he could think the application didn't properly refreshed
        DispatchQueue.global(qos: .background).async {
            dispatchGroup.enter()
            usleep(1500000)
            dispatchGroup.leave()
        }
        
        let highestEventId = DataBaseHelper.requestHighestId(entity: "Event", context: context)
        let highestClubId = DataBaseHelper.requestHighestId(entity: "Club",context: context)

        let url = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=\(highestEventId)&&idClub=\(highestClubId)"
        
        NSLog("Requesting data from %@", url)

        let urlObj = URL(string: url)
        
        //starts request - reply to the server with url depending on the highest stored id in the local db
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        dispatchGroup.enter()
        URLSession.shared.dataTask(with: urlObj!) {(data, response, error) in
            json = String(data: data!, encoding: String.Encoding.utf8)
            do {
                //jsonData is object of JSONData struct which contains an Object Array of Event- and Club-struct
                let jsonData = try JSONDecoder().decode(JSONDataStruct.self, from: data!)
                NSLog("JSONData object has been created")
                DataBaseHelper.saveRequestedArraysInDatabase(jsonDataObj: jsonData, context: context)
            }catch{
                //localizedDescription is needed to convert NSError into String
                NSLog("Requesting data from given URL has been unsuccessful. Error: %@", error.localizedDescription)
            }
            requestResponseServerIsRunning = false
            dispatchGroup.leave()
            }.resume()
    }
}
