//
//  FurniturteViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/16.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class FurnitureViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    var furnitureTable: UITableView!
    var tableData: [NSDictionary] = [NSDictionary]()
    
    //返回表格行数（也就是返回控件数）
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: FurnitureTableViewCell = furnitureTable.dequeueReusableCell(withIdentifier: "unboundFurnitureCell") as! FurnitureTableViewCell
        let item = tableData[indexPath.row]
        
        cell.furnitureName.text = item["furnName"] as? String
        cell.furnitureID.text = item["furnId"] as? String
        //cell.furnitureType.text = item["furnType"] as? String
        switch (item["furnType"] as! String) {
        case "1":
            cell.furnitureType.text = "灯具"
            cell.furnitureType.textColor = UIColor.blue
            break
        case "2":
            cell.furnitureType.text = "电视"
            cell.furnitureType.textColor = UIColor.purple
            break
        case "3":
            cell.furnitureType.text = "窗帘"
            cell.furnitureType.textColor = UIColor.orange
            break
        default:
            cell.furnitureType.text = "其他"
            cell.furnitureType.textColor = UIColor.red
            break
        }
        
        cell.parentView = self
        
        return cell
    }
    
    //在本例中，只有一个分区
    //func numberOfSectionsInTableView(tableView: UITableView) -> Int {
    //    return 1;
    //}
    
    //单元格高度
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 98.0
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "家具"
        
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)"
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/furniture/unbindList",
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let resultDictionary = response.result.value as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    let furnitures = resultDictionary["furnList"] as! NSDictionary
                    self.tableData = furnitures["list"] as! [NSDictionary]
                    
                    //创建表视图
                    self.furnitureTable = UITableView(frame: CGRect(x: 0, y: 60, width: self.view.frame.width, height: self.view.frame.height) , style: .plain)
                    //设置代理和数据源
                    self.furnitureTable!.delegate = self
                    self.furnitureTable!.dataSource = self
                    //设置表格背景色
                    self.furnitureTable!.backgroundColor = UIColor(red: 0xf0/255, green: 0xf0/255, blue: 0xf0/255, alpha: 1)
                    //去除单元格分隔线
                    self.furnitureTable!.separatorStyle = .none
                    //创建一个重用的单元格
                    self.furnitureTable!.register(UINib(nibName:"FurnitureTableViewCell", bundle:nil), forCellReuseIdentifier:"unboundFurnitureCell")
                    self.view.addSubview(self.furnitureTable!)
                    
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
