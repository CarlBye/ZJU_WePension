//
//  AddFurnitureViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/15.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class AddFurnitureViewController: UIViewController {
    
    @IBOutlet weak var furnitureID: UITextField!
    @IBOutlet weak var furnitureName: UITextField!
    var furnitureType: String!
    
    @IBAction func addFurniture() {
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)",
            "furnId":"\(furnitureID.text!)",
            "furnName":"\(furnitureName.text!)",
            "furnType":"\(furnitureType!)"
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/furniture/bind",
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let resultDictionary = response.result.value as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    
                    let alert = UIAlertController(title: "添加成功", message: "", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: {
                        action in
                        self.navigationController?.popViewController(animated: true)
                    })
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                    
                } else {
                    let alert = UIAlertController(title: "添加失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            } else {
                let alert = UIAlertController(title: "添加失败", message: "网络错误", preferredStyle: .alert)
                let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                alert.addAction(action)
                self.present(alert, animated: true, completion: nil)
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationItem.title = "添加家具"
        
        let defaultTitle = "选择类型"
        let choices = ["灯具", "电视", "窗帘", "其他"]
        let rect = CGRect(x: 50, y: 270, width: self.view.frame.width - 100, height: 50)
        let dropBoxView = TGDropBoxView(parentVC: self, title: defaultTitle, items: choices, frame: rect)
        dropBoxView.isHightWhenShowList = true
        dropBoxView.willShowOrHideBoxListHandler = { (isShow) in
//            if isShow { NSLog("will show choices") }
//            else { NSLog("will hide choices") }
        }
        dropBoxView.didShowOrHideBoxListHandler = { (isShow) in
//            if isShow { NSLog("did show choices") }
//            else { NSLog("did hide choices") }
        }
        dropBoxView.didSelectBoxItemHandler = { (row) in
            //NSLog("selected No.\(row): \(dropBoxView.currentTitle())")
            switch dropBoxView.currentTitle() {
            case "灯具":
                self.furnitureType = "1"
                break
            case "电视":
                self.furnitureType = "2"
                break
            case "窗帘":
                self.furnitureType = "3"
                break
            default:
                self.furnitureType = "0"
                break
            }
        }
        self.view.addSubview(dropBoxView)
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
