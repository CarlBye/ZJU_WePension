//
//  MyUnboundCommodityButtonViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/14.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class MyUnboundButtonViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

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
        let cell: MyUnboundButtonTableViewCell = buttonTable.dequeueReusableCell(withIdentifier: "unboundButtonCell") as! MyUnboundButtonTableViewCell
        let item = tableData[indexPath.row]
        cell.parentView = self
        
        cell.name.text = item["buttonName"] as? String
        cell.ID.text = item["buttonId"] as? String
        cell.button.setTitle("更改此按钮", for: .normal)
        //cell.button.setTitleColor(UIColor.orange, for: .normal)
        switch (item["buttonType"] as! String) {
        case "0":
            cell.type.text = "未设置绑定的按钮"
            cell.type.textColor = UIColor.red
            cell.button.setTitle("选择此按钮", for: .normal)
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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "按钮"
                                    
        //创建表视图
        self.buttonTable = UITableView(frame: CGRect(x: 0, y: 60, width: self.view.frame.width, height: self.view.frame.height) , style: .plain)
        //设置代理和数据源
        self.buttonTable!.delegate = self
        self.buttonTable!.dataSource = self
        //设置表格背景色
        self.buttonTable!.backgroundColor = UIColor(red: 0xf0/255, green: 0xf0/255, blue: 0xf0/255, alpha: 1)
        //去除单元格分隔线
        self.buttonTable!.separatorStyle = .none
        //创建一个重用的单元格
        self.buttonTable!.register(UINib(nibName:"MyUnboundButtonTableViewCell", bundle:nil), forCellReuseIdentifier:"unboundButtonCell")
        self.view.addSubview(self.buttonTable!)
        
    }
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
