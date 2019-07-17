//
//  CommodityTableViewCell.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/10.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class CommodityTableViewCell: UITableViewCell {
    
    @IBOutlet weak var cell: UIView!
    
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var name: UILabel!
    @IBOutlet weak var ID: UILabel!
    @IBOutlet weak var stock: UILabel!
    @IBOutlet weak var price: UILabel!
    
    var dbTableKey: String!
    var parentView: CommodityViewController! = nil
    
    @IBAction func bindCommodity() {
        UserDefaults().set(name.text, forKey: "nameOfSelectedCommodity")
        UserDefaults().set(dbTableKey, forKey: "keyOfSelectedCommodity")
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
