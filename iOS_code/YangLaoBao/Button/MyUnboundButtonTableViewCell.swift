//
//  MyUnboundButtonTableViewCell.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/14.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class MyUnboundButtonTableViewCell: UITableViewCell {
    
    var parentView: MyUnboundButtonViewController! = nil
    @IBOutlet weak var cell: UIView!
    
    @IBOutlet weak var name: UILabel!
    @IBOutlet weak var ID: UILabel!
    @IBOutlet weak var type: UILabel!
    @IBOutlet weak var button: UIButton!
    
    @IBAction func selectUnboundButton() {
        UserDefaults().set(name.text, forKey: "nameOfSelectedButton")
        UserDefaults().set(ID.text, forKey: "IDOfSelectedButton")
        parentView.navigationController?.popViewController(animated: true)
        return
    }

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
