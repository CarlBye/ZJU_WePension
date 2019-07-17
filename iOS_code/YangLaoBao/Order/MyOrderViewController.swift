//
//  MyOrderViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/15.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class MyOrderViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    var orderTable: UITableView!
    var tableData: [NSDictionary] = [NSDictionary]()

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: MyOrderTableViewCell = orderTable.dequeueReusableCell(withIdentifier: "orderCell") as! MyOrderTableViewCell
        let item = tableData[indexPath.row]

        let imgUrl = URL(string: item["comImgPath"] as! String!)
        let imgData = try! Data(contentsOf: imgUrl!)
        cell.img.image = UIImage(data: imgData)
        cell.commodityName.text = item["comName"] as? String
        cell.orderTime.text = item["orderDate"] as? String
        
        switch (item["orderState"] as! String) {
        case "1":
            cell.orderState.text = "待确认下单"
            cell.orderState.textColor = UIColor.red
            break
        case "2":
            cell.orderState.text = "运送中"
            cell.orderState.textColor = UIColor.orange
            break
        case "3":
            cell.orderState.text = "待签收"
            cell.orderState.textColor = UIColor.green
            break
        case "4":
            cell.orderState.text = "已完成"
            cell.orderState.textColor = UIColor.blue
            break
        default:
            break
        }
        
        cell.orderID = item["orderId"] as? String
        cell.orderQuantity = item["orderNum"] as? String
        cell.person = item["orderDeliveryName"] as? String
        cell.address = item["orderDeliveryAddress"] as? String
        cell.phone = item["orderDeliveryPhone"] as? String
        cell.commodityID = item["comNo"] as? String
        cell.commodityDBTableKey = item["comId"] as? String
        cell.orderPrice = item["comPrice"] as? String
        cell.commodityDescription = item["comDescription"] as? String
        cell.selfCommodity = item
        
        cell.parentView = self

        return cell
    }

    //在本例中，只有一个分区
    //func numberOfSectionsInTableView(tableView: UITableView) -> Int {
    //    return 1;
    //}
    
    //返回表格行数（也就是返回控件数）
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }

    //单元格高度
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 105.0
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if (UserDefaults().string(forKey: "isLogged") == "no") {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: String(describing: "login"))
                as! LoginViewController
            self.navigationController?.pushViewController(vc, animated: true)
        } else {
        
            let params = [
                "curId":"\(UserDefaults().string(forKey: "userID")!)",
                ] as [String : Any]
            let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
            Alamofire.request(url + "/api/commodity/orderList",
                              method: .post,
                              parameters: params,
                              encoding: JSONEncoding.default,
                              headers: headers).validate().responseJSON { response in
                if (response.result.isSuccess) {
                    let resultDictionary = response.result.value as! NSDictionary
                    if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                        
                        let orders = resultDictionary["orderList"] as! NSDictionary
                        self.tableData = orders["list"] as! [NSDictionary]
                        
                        //创建表视图
                        self.orderTable = UITableView(frame: CGRect(x: 0, y: 60, width: self.view.frame.width, height: self.view.frame.height-60) , style: .plain)
                        //设置代理和数据源
                        self.orderTable!.delegate = self
                        self.orderTable!.dataSource = self
                        //设置表格背景色
                        self.orderTable!.backgroundColor = UIColor(red: 0xf0/255, green: 0xf0/255, blue: 0xf0/255, alpha: 1)
                        //去除单元格分隔线
                        self.orderTable!.separatorStyle = .none
                        //创建一个重用的单元格
                        self.orderTable!.register(UINib(nibName:"MyOrderTableViewCell", bundle:nil), forCellReuseIdentifier:"orderCell")
                        self.view.addSubview(self.orderTable!)
                        
                    } else {
                        let alert = UIAlertController(title: "获取订单信息失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                        let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                        alert.addAction(action)
                        self.present(alert, animated: true, completion: nil)
                    }
                } else {
                    let alert = UIAlertController(title: "获取订单信息失败", message: "网络错误", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "我的订单"

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }


}
