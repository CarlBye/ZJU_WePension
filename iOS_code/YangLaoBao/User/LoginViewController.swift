//
//  LogInViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/11.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class LoginViewController: UIViewController {

    @IBOutlet var userTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //userTextField.keyboardType = UIKeyboardType.default
        self.navigationItem.title = "登录"
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func logIn() {
        let user = userTextField.text
        let password = passwordTextField.text
        
        if ((user?.isEmpty)! || (password?.isEmpty)!) {
            let alert = UIAlertController(title: "登录失败", message: "账号或密码不能为空", preferredStyle: .alert)
            let action = UIAlertAction(title: "确定", style: .default, handler: nil)
            alert.addAction(action)
            present(alert, animated: true, completion: nil)
        } else {
            let params = [
                "logName":"\(user!)",
                "logPwd":"\(password!)",
                ] as [String : Any]
            let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
            Alamofire.request(url + "/api/user/login",
                              method: .post,
                              parameters: params,
                              encoding: JSONEncoding.default,
                              headers: headers).validate().responseJSON { response in
                if (response.result.isSuccess) {
                    let result = response.result.value
                    let resultDictionary = result as! NSDictionary
                    if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                        UserDefaults().set(String(describing: resultDictionary["curId"]!), forKey: "userID")
                        UserDefaults().set(String(describing: resultDictionary["curName"]!), forKey: "userName")
                        UserDefaults().set(String(describing: resultDictionary["curPhone"]!), forKey: "userPhone")
                        UserDefaults().set(String(describing: resultDictionary["curEmail"]!), forKey: "userEmail")
                        UserDefaults().set(String(describing: resultDictionary["curFaceId"]!), forKey: "userAvatarID")
                        UserDefaults().set(String(describing: resultDictionary["curDescription"]!), forKey: "userDescription")
                        UserDefaults().set(String(describing: resultDictionary["curAlert"]!), forKey: "userEmergencyPhone")
                        UserDefaults().set("yes", forKey: "isLogged")
                        self.navigationController?.popViewController(animated: true)
                    } else {
                        let alert = UIAlertController(title: "登录失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                        let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                        alert.addAction(action)
                        self.present(alert, animated: true, completion: nil)
                    }
                } else {
                    let alert = UIAlertController(title: "登录失败", message: "网络错误", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            }
        }
        return
    }
    
    @IBAction func jumpToRegister() {
        //dismiss(animated: true, completion: nil)
        //self.present(RegisterViewController(), animated: true, completion: nil)
        //performSegue(withIdentifier: "gotoRegister", sender: nil)
        //self.navigationController?.popViewController(animated: true)
        let vc = self.storyboard?.instantiateViewController(withIdentifier: String(describing: "register"))
            as! RegisterViewController
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    //键盘可隐藏
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        view.endEditing(true)
    }
    
    //    //选择头像的函数
    //    func selectIcon(){
    //        let userIconAlert = UIAlertController(title: "请选择操作", message: "", preferredStyle: UIAlertControllerStyle.actionSheet)
    //
    //        let chooseFromPhotoAlbum = UIAlertAction(title: "从相册选择", style: UIAlertActionStyle.default, handler: funcChooseFromPhotoAlbum)
    //        userIconAlert.addAction(chooseFromPhotoAlbum)
    //
    //        let chooseFromCamera = UIAlertAction(title: "拍照", style: UIAlertActionStyle.default,handler:funcChooseFromCamera)
    //        userIconAlert.addAction(chooseFromCamera)
    //
    //        let canelAction = UIAlertAction(title: "取消", style: UIAlertActionStyle.cancel,handler: nil)
    //        userIconAlert.addAction(canelAction)
    //
    //        self.present(userIconAlert, animated: true, completion: nil)
    //    }
    //
    //    //从相册选择照片
    //    func funcChooseFromPhotoAlbum(avc:UIAlertAction) -> Void{
    //        let imagePicker = UIImagePickerController()
    //        //设置代理
    //        imagePicker.delegate = self
    //        //允许编辑
    //        imagePicker.allowsEditing = true
    //        //设置图片源
    //        imagePicker.sourceType = UIImagePickerControllerSourceType.photoLibrary
    //        //模态弹出IamgePickerView
    //        self.present(imagePicker, animated: true, completion: nil)
    //    }
    //
    //    //拍摄照片
    //    func funcChooseFromCamera(avc:UIAlertAction) -> Void{
    //        let imagePicker = UIImagePickerController()
    //        //设置代理
    //        imagePicker.delegate = self
    //        //允许编辑
    //        imagePicker.allowsEditing=true
    //        //设置图片源
    //        imagePicker.sourceType = UIImagePickerControllerSourceType.camera
    //        //模态弹出IamgePickerView
    //        self.present(imagePicker, animated: true, completion: nil)
    //    }
    //
    //    func imagePickerControllerDidCancel(_ picker: UIImagePickerController){
    //        picker.dismiss(animated: true, completion: nil)
    //    }
    //
    //    //UIImagePicker回调方法
    //    private func imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
    //        //获取照片的原图
    //        //let image = (info as NSDictionary).objectForKey(UIImagePickerControllerOriginalImage)
    //        //获得编辑后的图片
    //        let image = (info as NSDictionary).object(forKey: UIImagePickerControllerEditedImage)
    //        //保存图片至沙盒
    //        self.saveImage(currentImage: image as! UIImage, imageName: iconImageFileName)
    //        let fullPath = ((NSHomeDirectory() as NSString).appendingPathComponent("Documents") as NSString).appendingPathComponent(iconImageFileName)
    //        //存储后拿出更新头像
    //        let savedImage = UIImage(contentsOfFile: fullPath)
    //        self.icon.image=savedImage
    //        picker.dismiss(animated: true, completion: nil)
    //    }
    //
    //    //MARK: - 保存图片至沙盒
    //    func saveImage(currentImage:UIImage,imageName:String){
    //        var imageData = NSData()
    //        imageData = UIImageJPEGRepresentation(currentImage, 0.5)! as NSData
    //        // 获取沙盒目录
    //        let fullPath = ((NSHomeDirectory() as NSString).appendingPathComponent("Documents") as NSString).appendingPathComponent(imageName)
    //        // 将图片写入文件
    //        imageData.write(toFile: fullPath, atomically: false)
    //    }

}
