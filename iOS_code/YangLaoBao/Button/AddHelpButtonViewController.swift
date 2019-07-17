//
//  AddHelpButtonViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/16.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class AddHelpButtonViewController: UIViewController {

    @IBOutlet var bindingButton: UILabel!
    @IBOutlet var bindingHelpInfomation: UITextView!
    @IBOutlet var bindingHelpPhone: UITextField!
    
    @IBAction func submit() {
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)",
            "buttonId":"\(UserDefaults().string(forKey: "IDOfSelectedButton")!)",
            "alertPhone":"\(bindingHelpPhone.text!)",
            "alertMessage":"\(bindingHelpInfomation.text!)"
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/button/bind/alert",
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
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "求助按钮"
        
        UserDefaults().set("尚未选择", forKey: "nameOfSelectedButton")
        bindingHelpPhone.text = UserDefaults().string(forKey: "userPhone")!
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

}
