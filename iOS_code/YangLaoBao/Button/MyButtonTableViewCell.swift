//
//  MyButtonTableViewCell.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/14.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class MyButtonTableViewCell: UITableViewCell {

    var parentView: MyButtonViewController! = nil
    @IBOutlet weak var cell: UIView!
    
    @IBOutlet weak var name: UILabel!
    @IBOutlet weak var ID: UILabel!
    @IBOutlet weak var type: UILabel!
    @IBOutlet weak var switchBtn: UISwitch!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
}
