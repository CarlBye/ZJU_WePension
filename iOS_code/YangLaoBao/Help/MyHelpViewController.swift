//
//  MyHelpViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/16.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class MyHelpViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    var helpTable: UITableView!
    var tableData: [NSDictionary] = [NSDictionary]()
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell: MyHelpTableViewCell = helpTable.dequeueReusableCell(withIdentifier: "helpCell") as! MyHelpTableViewCell
        let item = tableData[indexPath.row]
        
        cell.information.text = item["historyMessage"] as? String
        cell.time.text = item["historyDate"] as? String
        if (item["isConfirmed"] as? Bool == true) {
            cell.state.text = "已确认"
            cell.state.textColor = UIColor.green
            cell.confirmButton.isEnabled = false;
            cell.confirmButton.setTitle("确认处理", for: .normal)
        } else {
            cell.state.text = "等待处理"
            cell.state.textColor = UIColor.red
            cell.confirmButton.setTitle("确认处理", for: .normal)
        }
        cell.phone = item["historyPhone"] as? String
        cell.historyID = item["historyId"] as? String
        
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
        return 140.0
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
            Alamofire.request(url + "/api/alert/list",
                              method: .post,
                              parameters: params,
                              encoding: JSONEncoding.default,
                              headers: headers).validate().responseJSON { response in
                if (response.result.isSuccess) {
                    let resultDictionary = response.result.value as! NSDictionary
                    if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                        
                        let helps = resultDictionary["alertHistory"] as! NSDictionary
                        self.tableData = helps["list"] as! [NSDictionary]
                        
                        //创建表视图
                        self.helpTable = UITableView(frame: CGRect(x: 0, y: 60, width: self.view.frame.width, height: self.view.frame.height-200) , style: .plain)
                        //设置代理和数据源
                        self.helpTable!.delegate = self
                        self.helpTable!.dataSource = self
                        //设置表格背景色
                        self.helpTable!.backgroundColor = UIColor(red: 0xf0/255, green: 0xf0/255, blue: 0xf0/255, alpha: 1)
                        //去除单元格分隔线
                        //self.helpTable!.separatorStyle = .none
                        // 将分割线四周边距设为0
                        self.helpTable!.separatorInset = .zero
                        // 以边框为参照点设置分割线边距
                        self.helpTable!.separatorInsetReference = .fromCellEdges
                        //创建一个重用的单元格
                        self.helpTable!.register(UINib(nibName:"MyHelpTableViewCell", bundle:nil), forCellReuseIdentifier:"helpCell")
                        self.view.addSubview(self.helpTable!)
                        
                    } else {
                        let alert = UIAlertController(title: "获取求助信息失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                        let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                        alert.addAction(action)
                        self.present(alert, animated: true, completion: nil)
                    }
                } else {
                    let alert = UIAlertController(title: "获取求助信息失败", message: "网络错误", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "我的求助"

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
}
