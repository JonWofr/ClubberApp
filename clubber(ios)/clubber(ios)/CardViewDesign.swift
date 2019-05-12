//
//  CardViewEvents.swift
//  clubber(ios)
//
//  Created by Arbeiten on 06.05.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import UIKit

//class to change the corner radius, the opacity and the shadow of the cells

@IBDesignable class CardViewDesign: UIView {
    
    @IBInspectable var cornerRadius : CGFloat = 4
    @IBInspectable var shadowOffSetWidth : CGFloat = 0
    @IBInspectable var shadowOffSetHeight : CGFloat = 2
    @IBInspectable var shadowColor : UIColor = UIColor.black
    @IBInspectable var shadowOpacity : Float = 0.2
    
    
    override func layoutSubviews() {
        
        layer.cornerRadius = cornerRadius
        layer.shadowColor = shadowColor.cgColor
        layer.shadowOffset = CGSize(width: shadowOffSetWidth, height: shadowOffSetHeight)
        let shadowPath = UIBezierPath(roundedRect: bounds, cornerRadius: cornerRadius)
        layer.shadowPath = shadowPath.cgPath
        layer.shadowOpacity = shadowOpacity
    }



}
