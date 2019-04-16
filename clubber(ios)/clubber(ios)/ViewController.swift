//
//  ViewController.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import UIKit
import CoreData

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        super.viewDidLoad()
        
       
        
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        
        
        let context = appDelegate.persistentContainer.viewContext
        
        //let newUser = NSEntityDescription.insertNewObject(forEntityName: "Events", into: context)
        
    
        /*
        newUser.setValue("nameTest", forKey: "name")
        newUser.setValue("12.04.19", forKey: "date")
        newUser.setValue(1, forKey: "id")
        newUser.setValue("7grad", forKey: "club")
        newUser.setValue("genreTest", forKey: "genre")
        newUser.setValue("07:00", forKey: "srttime")
        newUser.setValue("link", forKey: "btn")
        
        do{
            
            try context.save()
            print("SAVED")
            
            
            
        }catch{
            
        }
        
    }
 */
        
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "Events")
        request.returnsObjectsAsFaults = false
        
        
        do{
            let results = try context.fetch(request)
        
            if(results.count > 0){
                
                for event in results {
                    print((event as AnyObject).value(forKey: "date") as! String)
                }
            
        }
        
        }catch{
            
        }
        
        

}

}
