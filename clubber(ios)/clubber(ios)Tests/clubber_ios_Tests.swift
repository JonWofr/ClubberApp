//
//  clubber_ios_Tests.swift
//  clubber(ios)Tests
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import XCTest
import CoreData
@testable import clubber_ios_

class clubber_ios_Tests: XCTestCase {

    var databaseHelper: DataBaseHelper!

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() {
        // This is an example of a functional test case.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
    }

    func testPerformanceExample() {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }
    
    func test_create_event() {
        
        //Given the name & ssn
        let name1 = "Alok"
        let ssn1 = 123
        
        let name2 = "Naitvik"
        let ssn2 = 456
        
        let name3 = "Deepti"
        let ssn3 = 789
        
        let person1 = coreDataManager.insertPerson(name: name1, ssn: Int16(ssn1))
        
        /*Asserts that an expression is not nil.
         Generates a failure when expression == nil.*/
        XCTAssertNotNil( person1 )
        
        let person2 = coreDataManager.insertPerson(name: name2, ssn: Int16(ssn2))
        
        /*Asserts that an expression is not nil.
         Generates a failure when expression == nil.*/
        XCTAssertNotNil( person2 )
        
        let person3 = coreDataManager.insertPerson(name: name3, ssn: Int16(ssn3))
        
        /*Asserts that an expression is not nil.
         Generates a failure when expression == nil.*/
        XCTAssertNotNil( person3 )
        
    }

}
