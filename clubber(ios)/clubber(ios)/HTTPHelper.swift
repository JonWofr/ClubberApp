//
//  HTTPHelper.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 14.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import Foundation


class HTTPHelper {
    struct JSONData : Decodable {
        var events :[Event]!
        var clubs : [Club]!
    }
    
    struct Event : Decodable {
        var id : String!
        var date : String!
        var name : String!
    }
    
    struct Club : Decodable {
        var id : String!
        var name : String!
    }
    
    static func requestResponseServer(){
        let url = "https://clubber-stuttgart.de/script/scriptDB.php"
        let urlObj = URL(string: url)
        
        URLSession.shared.dataTask(with: urlObj!) {(data, response, error) in
            do {
                let jsonData = try JSONDecoder().decode(JSONData.self, from: data!)
                
                for event in jsonData.events {
                    print(event.name)
                }
                for club in jsonData.clubs {
                    print(club.name)
                }
                
            }catch{
                print("ooooooppps!")
            }
            
            }.resume()
    }
}
