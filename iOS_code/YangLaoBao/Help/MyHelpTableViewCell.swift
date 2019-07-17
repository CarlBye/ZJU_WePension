//
//  MyHelpTableViewCell.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/16.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class MyHelpTableViewCell: UITableViewCell {
    
    @IBOutlet weak var cell: UIView!
    
    @IBOutlet weak var information: UITextView!
    @IBOutlet weak var time: UILabel!
    @IBOutlet weak var state: UILabel!
    @IBOutlet weak var confirmButton: UIButton!
    var historyID: String!
    var phone: String!
    
    var parentView: MyHelpViewController! = nil
    
    @IBAction func confirm() {
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)",
            "historyId":"\(self.historyID!)"
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/alert/changeState",
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let resultDictionary = response.result.value as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    let alert = UIAlertController(title: "您已确认处理！", message: "", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: {
                        action in
                        self.parentView.navigationController?.popViewController(animated: true)
                    })
                    alert.addAction(action)
                    self.parentView.present(alert, animated: true, completion: nil)
                } else {
                    let alert = UIAlertController(title: "确认失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.parentView.present(alert, animated: true, completion: nil)
                }
            } else {
                let alert = UIAlertController(title: "确认失败", message: "网络错误", preferredStyle: .alert)
                let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                alert.addAction(action)
                self.parentView.present(alert, animated: true, completion: nil)
            }
        }
    }

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
