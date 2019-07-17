//
//  MyOrderDetailsViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/15.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

var curCommodity: NSDictionary = NSDictionary()

class MyOrderDetailsViewController: UIViewController {
    
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var commodityName: UILabel!
    @IBOutlet weak var commodityID: UILabel!
    @IBOutlet weak var commodityDescription: UILabel!
    
    @IBOutlet weak var orderTime: UILabel!
    @IBOutlet weak var orderQuantity: UILabel!
    @IBOutlet weak var orderPrice: UILabel!
    
    @IBOutlet weak var person: UILabel!
    @IBOutlet weak var address: UILabel!
    @IBOutlet weak var phone: UILabel!
    
    @IBOutlet weak var orderState: UILabel!
    
    var orderID: String!
    var commodityDBTableKey: String!
    
    @IBOutlet weak var cancel: UIButton!
    @IBOutlet weak var confirm: UIButton!
    
    @IBAction func cancelOrder() {
        // TODO
    }
    
    @IBAction func confirmOrder() {
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)",
            "orderId":"\(orderID!)",
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/commodity/changeOrderState",
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let result = response.result.value
                let resultDictionary = result as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    self.navigationController?.popViewController(animated: true)
                } else {
                    let alert = UIAlertController(title: "确认失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            } else {
                let alert = UIAlertController(title: "确认失败", message: "网络错误", preferredStyle: .alert)
                let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                alert.addAction(action)
                self.present(alert, animated: true, completion: nil)
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationItem.title = "订单详情"
        
        let imgUrl = URL(string: curCommodity["comImgPath"] as! String!)
        let imgData = try! Data(contentsOf: imgUrl!)
        img.image = UIImage(data: imgData)
        commodityName.text = curCommodity["comName"] as? String
        commodityID.text = curCommodity["comNo"] as? String
        commodityDescription.text = curCommodity["comDescription"] as? String
        orderTime.text = curCommodity["orderDate"] as? String
        orderPrice.text = curCommodity["comPrice"] as? String
        orderQuantity.text = curCommodity["orderNum"] as? String
        person.text = curCommodity["orderDeliveryName"] as? String
        address.text = curCommodity["orderDeliveryAddress"] as? String
        phone.text = curCommodity["orderDeliveryPhone"] as? String
        switch (curCommodity["orderState"] as! String) {
        case "1":
            orderState.text = "待确认下单"
            orderState.textColor = UIColor.red
            cancel.setTitle("取消订单", for: .normal)
            cancel.isEnabled = true;
            confirm.setTitle("确认下单", for: .normal)
            confirm.isEnabled = true;
            break
        case "2":
            orderState.text = "运送中"
            orderState.textColor = UIColor.orange
            cancel.setTitle("", for: .normal)
            cancel.isEnabled = false;
            confirm.setTitle("", for: .normal)
            confirm.isEnabled = false;
            break
        case "3":
            orderState.text = "待签收"
            orderState.textColor = UIColor.green
            cancel.setTitle("", for: .normal)
            cancel.isEnabled = false;
            confirm.setTitle("确认签收", for: .normal)
            confirm.isEnabled = true;
            break
        case "4":
            orderState.text = "已完成"
            orderState.textColor = UIColor.blue
            cancel.setTitle("", for: .normal)
            cancel.isEnabled = false;
            confirm.setTitle("", for: .normal)
            confirm.isEnabled = false;
            break
        default:
            break
        }
        
        orderID = curCommodity["orderId"] as? String
        commodityDBTableKey = curCommodity["comId"] as? String
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
