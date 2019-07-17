//
//  myButtonViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/13.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

var tableData: [NSDictionary] = [NSDictionary]()

// UITableViewDelegate, UITableViewDataSource 为表格协议
class MyButtonViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    var buttonTable: UITableView!
    
    //返回表格行数（控件数）
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    //单元格高度
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 108.0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: MyButtonTableViewCell = buttonTable.dequeueReusableCell(withIdentifier: "buttonCell") as! MyButtonTableViewCell
        let item = tableData[indexPath.row]
        cell.parentView = self
        
        cell.name.text = item["buttonName"] as? String
        cell.ID.text = item["buttonId"] as? String
        switch (item["buttonType"] as! String) {
        case "0":
            cell.type.text = "未设置绑定的按钮"
            cell.type.textColor = UIColor.red
            break
        case "1":
            cell.type.text = "购物按钮"
            cell.type.textColor = UIColor.blue
            break
        case "2":
            cell.type.text = "家具按钮"
            cell.type.textColor = UIColor.purple
            break
        case "3":
            cell.type.text = "求助按钮"
            cell.type.textColor = UIColor.orange
            break
        case "4":
            cell.type.text = "监测按钮"
            cell.type.textColor = UIColor.green
            break
        default:
            break
        }
        
        return cell
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.navigationItem.title = "我的按钮"
        
        if (UserDefaults().string(forKey: "isLogged") == "no") {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: String(describing: "login"))
                as! LoginViewController
            self.navigationController?.pushViewController(vc, animated: true)
        } else {
            let params = [
                "curId":"\(UserDefaults().string(forKey: "userID")!)"
                ] as [String : Any]
            let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
            Alamofire.request(url + "/api/button/list",
                            method: .post,
                            parameters: params,
                            encoding: JSONEncoding.default,
                            headers: headers).validate().responseJSON { response in
                if (response.result.isSuccess) {
                    let resultDictionary = response.result.value as! NSDictionary
                    if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                        let buttons = resultDictionary["buttonList"] as! NSDictionary
                        tableData = buttons["list"] as! [NSDictionary]

                        //创建表视图
                        self.buttonTable = UITableView(frame: CGRect(x: 0, y: 60, width: self.view.frame.width, height: self.view.frame.height-210) , style: .plain)
                        //设置代理和数据源
                        self.buttonTable!.delegate = self
                        self.buttonTable!.dataSource = self
                        //设置表格背景色
                        self.buttonTable!.backgroundColor = UIColor(red: 0xf0/255, green: 0xf0/255, blue: 0xf0/255, alpha: 1)
                        //去除单元格分隔线
                        //self.buttonTable!.separatorStyle = .none
                        // 将分割线四周边距设为0
                        self.buttonTable.separatorInset = .zero
                        // 以边框为参照点设置分割线边距
                        self.buttonTable.separatorInsetReference = .fromCellEdges
                        //创建一个重用的单元格
                        self.buttonTable!.register(UINib(nibName:"MyButtonTableViewCell", bundle:nil), forCellReuseIdentifier:"buttonCell")
                        self.view.addSubview(self.buttonTable!)
                        
                    } else {
                        let alert = UIAlertController(title: "获取按钮信息失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
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
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "我的按钮"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
