#API return literature item 

import time
import json
import toolz
from selenium import webdriver
class product:  
    def __init__(self, product_name, img_url, p_price, p_url ):  
        self.product_name = product_name  
        self.img_url = img_url
        self.p_price = p_price
        self.p_url = p_url
        

def empik(productlist, input_name):
    
     
    url = "https://www.empik.com/ksiazki,31,s?q=" + input_name + "&qtype=basicForm"
    
    browser = webdriver.Chrome()
    browser.get(url)
    elements = browser.find_elements_by_class_name("productWrapper")[:20]
    for a in elements:
        time.sleep(0.2)
        try:
            p_name = a.find_element_by_class_name("ta-product-title").text.replace("\n","")
            
            p_price = a.find_element_by_class_name("price.ta-price-tile").text.strip().split("zł")[0]
            
            p_img_url = a.find_element_by_class_name("lazy").get_attribute("src")
            
            p_url = a.find_element_by_class_name("img.seoImage").get_attribute("href")
            
        except:
            
            return False
            
        productlist.append(product(p_name, p_img_url, p_price, p_url))     
    browser.close()
    
def inverso(productlist, input_name):

    url = "https://inverso.pl/s?q=" + input_name
    
    browser = webdriver.Chrome()
    browser.get(url)
    time.sleep(2)
    browser.find_element_by_class_name("btn-back").click()
    elements = browser.find_elements_by_class_name("product-box.gallery.clearfix")[:20]
    for a in elements:
        time.sleep(0.2)
        try:
            p_name = a.find_element_by_class_name("product-name").text.strip()
            
            p_price = a.find_element_by_class_name("base-price").text.strip().split("zł")[0]
            
            p_img_url = a.find_element_by_class_name("blazy.b-loaded").get_attribute("src")
            
            p_url = a.find_element_by_class_name("product-hover-opacity").get_attribute("href")
            
        except:
            
            return False
            
        productlist.append(product(p_name, p_img_url, p_price, p_url))     
    browser.close()

    
def main(input_name):
    productlist = []
    inverso(productlist, input_name)
    empik(productlist, input_name)
    
    productlist = toolz.unique(productlist, key=lambda x: x.product_name)

    json_string = json.dumps([ob.__dict__ for ob in productlist], ensure_ascii=False).encode('utf8')
    return json_string
 


