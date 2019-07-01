//
//  clubber_ios_UITests.swift
//  clubber(ios)UITests
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import XCTest

class clubber_ios_UITests: XCTestCase {

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.

        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false

        // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
        XCUIApplication().launch()

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() {
       
        let app = XCUIApplication()
        app.textFields["Datum auswählen"].tap()
        
        //picks the 8. Dezember 2019
        let datePickers = app.datePickers
        datePickers.pickerWheels.element(boundBy: 0).adjust(toPickerWheelValue: "8.")
        datePickers.pickerWheels.element(boundBy: 1).adjust(toPickerWheelValue: "Dezember")
        datePickers.pickerWheels.element(boundBy: 2).adjust(toPickerWheelValue: "2019")
        app.toolbars["Toolbar"].buttons["Done"].tap()
        app.buttons["Events anzeigen"].tap()
        //check if the cell exists
        app.tables.cells.children(matching: .other).element(boundBy: 2).tap()
        
        let tabBarsQuery = app.tabBars
        tabBarsQuery.buttons["Clubs"].tap()
        tabBarsQuery.buttons["Events"].tap()
    }

}
