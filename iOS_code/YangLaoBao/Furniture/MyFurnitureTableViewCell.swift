//
//  MyFurnitureTableViewCell.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/15.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class MyFurnitureTableViewCell: UITableViewCell {
    
    @IBOutlet weak var cell: UIView!
    
    @IBOutlet weak var furnitureName: UILabel!
    @IBOutlet weak var furnitureType: UILabel!
    @IBOutlet weak var furnitureID: UILabel!
    @IBOutlet weak var furnitureState: UISwitch!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
