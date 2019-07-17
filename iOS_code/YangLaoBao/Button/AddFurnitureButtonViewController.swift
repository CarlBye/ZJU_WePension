//
//  AddFurnitureButtonViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/14.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class AddFurnitureButtonViewController: UIViewController {
    
    @IBOutlet var bindingButton: UILabel!
    @IBOutlet var bindingFurnitureID: UILabel!
    @IBOutlet var bindingFurnitureName: UILabel!
    
    @IBAction func submit() {
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)",
            "buttonId":"\(UserDefaults().string(forKey: "IDOfSelectedButton")!)",
            "furnId":"\(UserDefaults().string(forKey: "IDOfSelectedFurniture")!)",
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/button/bind/furniture",
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let result = response.result.value
                let resultDictionary = result as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    let alert = UIAlertController(title: "绑定成功", message: "", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: {
                        action in
                        self.navigationController?.popViewController(animated: true)
                    })
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                } else {
                    let alert = UIAlertController(title: "绑定失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            } else {
                let alert = UIAlertController(title: "绑定失败", message: "网络错误", preferredStyle: .alert)
                let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                alert.addAction(action)
                self.present(alert, animated: true, completion: nil)
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        bindingButton.text = UserDefaults().string(forKey: "nameOfSelectedButton")!
        bindingFurnitureID.text = UserDefaults().string(forKey: "IDOfSelectedFurniture")!
        bindingFurnitureName.text = UserDefaults().string(forKey: "nameOfSelectedFurniture")!
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationItem.title = "家具按钮"
        
        UserDefaults().set("尚未选择", forKey: "nameOfSelectedButton")
        UserDefaults().set("尚未绑定", forKey: "IDOfSelectedFurniture")
        UserDefaults().set("家具", forKey: "nameOfSelectedFurniture")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
}
