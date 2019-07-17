//
//  CommodityViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/9.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class CommodityViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    var commodityTable: UITableView!
    var tableData: [NSDictionary] = [NSDictionary]()
    
    //返回表格行数（也就是返回控件数）
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: CommodityTableViewCell = commodityTable.dequeueReusableCell(withIdentifier: "commodityCell") as! CommodityTableViewCell
        let item = tableData[indexPath.row]
        cell.parentView = self

        let imgUrl = URL(string: item["comImgPath"] as! String!)
        let imgData = try! Data(contentsOf: imgUrl!)
        cell.img.image = UIImage(data: imgData)
        cell.name.text = item["comName"] as? String
        cell.ID.text = item["comNo"] as? String
        cell.stock.text = item["comStack"] as? String
        cell.price.text = item["comPrice"] as? String
        cell.price.text?.append(" $")
        cell.dbTableKey = item["comId"] as? String
        return cell
    }
    
    //在本例中，只有一个分区
    //func numberOfSectionsInTableView(tableView: UITableView) -> Int {
    //    return 1;
    //}
    
    //单元格高度
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 150.0
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "商品"
        
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/commodity/list",
                        method: .post,
                        encoding: JSONEncoding.default,
                        headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let resultDictionary = response.result.value as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    let commoditys = resultDictionary["commodityList"] as! NSDictionary
                    self.tableData = commoditys["list"] as! [NSDictionary]

                    //创建表视图
                    self.commodityTable = UITableView(frame: CGRect(x: 0, y: 120, width: self.view.frame.width, height: self.view.frame.height) , style: .plain)
                    //设置代理和数据源
                    self.commodityTable!.delegate = self
                    self.commodityTable!.dataSource = self
                    //设置表格背景色
                    self.commodityTable!.backgroundColor = UIColor(red: 0xf0/255, green: 0xf0/255, blue: 0xf0/255, alpha: 1)
                    //去除单元格分隔线
                    self.commodityTable!.separatorStyle = .none
                    //创建一个重用的单元格
                    self.commodityTable!.register(UINib(nibName:"CommodityTableViewCell", bundle:nil), forCellReuseIdentifier:"commodityCell")
                    self.view.addSubview(self.commodityTable!)
                } else {
                    let alert = UIAlertController(title: "获取商品信息失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            } else {
                let alert = UIAlertController(title: "获取商品信息失败", message: "网络错误", preferredStyle: .alert)
                let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                alert.addAction(action)
                self.present(alert, animated: true, completion: nil)
            }
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
