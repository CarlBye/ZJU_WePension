//
//  MyOrderTableViewCell.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/15.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class MyOrderTableViewCell: UITableViewCell {

    @IBOutlet weak var cell: UIView!
    
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var commodityName: UILabel!
    @IBOutlet weak var orderTime: UILabel!
    @IBOutlet weak var orderState: UILabel!
    var orderID: String!
    var orderQuantity: String!
    var orderPrice: String!
    var person: String!
    var address: String!
    var phone: String!
    var commodityID: String!
    var commodityDBTableKey: String!
    var commodityDescription: String!
    
    var parentView: MyOrderViewController! = nil
    var selfCommodity: NSDictionary = NSDictionary()
    
    @IBAction func checkOrder() {
        let vc = parentView.storyboard?.instantiateViewController(withIdentifier: String(describing: "orderDetails"))
            as! MyOrderDetailsViewController
  
        curCommodity = self.selfCommodity
        
        parentView.navigationController?.pushViewController(vc, animated: true)
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
}
