//
//  FurnitureTableViewCell.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/16.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class FurnitureTableViewCell: UITableViewCell {
    
    @IBOutlet weak var cell: UIView!
    
    @IBOutlet weak var furnitureName: UILabel!
    @IBOutlet weak var furnitureType: UILabel!
    @IBOutlet weak var furnitureID: UILabel!
    
    var parentView: FurnitureViewController! = nil
    
    @IBAction func bindFurniture() {
        UserDefaults().set(furnitureID.text, forKey: "IDOfSelectedFurniture")
        UserDefaults().set(furnitureName.text, forKey: "nameOfSelectedFurniture")
        parentView.navigationController?.popViewController(animated: true)
        return
    }

    override func awakeFromNib() {
        super.awakeFromNib()
       
        //设置cell是有圆角边框显示
        cell.layer.cornerRadius = 8
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
