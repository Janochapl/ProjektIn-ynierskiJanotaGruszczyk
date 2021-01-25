#API return list 

import requests
from bs4 import BeautifulSoup
import time
import json
import toolz
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

class productInList:  
    def __init__(self, product_name, img_url, info_text ):  
        self.product_name = product_name  
        self.img_url = img_url
        self.info_text = info_text
        
def electronic(input_name):
    
    productlist = []
     
    url = "https://www.euro.com.pl/search.bhtml?keyword=" + input_name + "&searchType=Tag#fromKeyword=" + input_name
    
    page = requests.get(url)
    
    soup = BeautifulSoup(page.content, 'html.parser')
    
    for a in soup.find_all("div", {"class": "product-row"}):
        try:
            p_list = a.find("a", {"class":"js-save-keyword"}).text.strip().replace("\u200c","").split(" ")[:3]
            p_name =  ' '.join([str(elem) for elem in p_list]) 
            p_img_url = a.find("div",{"class":"product-photo"}).a.img["src"]
            if(p_img_url == "/img/desktop/empty.png"):
                p_img_url = a.find("div",{"class":"product-photo"}).a.img["data-original"]
            info_div = a.find_all("div", {"class":"attributes-row"})
            p_info_text=""
            for row in info_div:
                atr_name = row.span.text.strip()
                atr_value = row.select("span")[1].text.strip()
                p_info_text = p_info_text + atr_name + ": "+ atr_value + ";"        
        except:
             return False
            
        productlist.append(productInList(p_name, p_img_url, p_info_text))  

    print(len(productlist))
    productlist = toolz.unique(productlist, key=lambda x: x.product_name)

    json_string = json.dumps([ob.__dict__ for ob in productlist], ensure_ascii=False).encode('utf8')
    return json_string

def wear(input_name):
    
    productlist = []
     
    url = "https://domodi.pl/wyszukaj?search=" + input_name + "&Sort=score"
    
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')
    
    for a in soup.find_all("div", {"class": "dm-card__wrap"})[:20]:
        
        try:
            p_name = a.find("h4", {"class":"_dbl dm-1of1"}).a.text.strip().split("-",1)[0]
            p_info_text = a.find("strong", {"class":"_dbl dm-1of1"}).a.text.strip()
            p_img_url = a.find("div", {"class":"dm-card__hold"}).a.img["src"]
            if(p_img_url == "/content/img/blank.gif"):
                p_img_url = a.find("div", {"class":"dm-card__hold"}).a.img["data-original"]       
        except:
             return False
            
        productlist.append(productInList(p_name, p_img_url, p_info_text))  

    print(len(productlist))
    productlist = toolz.unique(productlist, key=lambda x: x.product_name)

    json_string = json.dumps([ob.__dict__ for ob in productlist], ensure_ascii=False).encode('utf8')
    return json_string

def house(input_name):
    
    productlist = []
     
    url = "https://www.castorama.pl/result/?q=" + input_name
    
    browser = webdriver.Chrome()
    browser.get(url)

    elements = browser.find_elements_by_class_name("sn-product--wrp")[:20]
    for a in elements:
        
        try:
            p_name = a.find_element_by_class_name("product-name").text
            
            p_info_text = ""
            
            p_img_url = a.find_element_by_tag_name("img").get_attribute("src")
            
        except:
            
            return False
            
        productlist.append(productInList(p_name, p_img_url, p_info_text))  
    browser.close()
    
    productlist = toolz.unique(productlist, key=lambda x: x.product_name)

    json_string = json.dumps([ob.__dict__ for ob in productlist], ensure_ascii=False).encode('utf8')
    return json_string
    
    
def literature(input_name):
    
    productlist = []
     
    url = "https://www.empik.com/ksiazki,31,s?q=" + input_name + "&qtype=basicForm"
    
    browser = webdriver.Chrome()
    browser.get(url)

    elements = browser.find_elements_by_class_name("productWrapper")[:20]
    for a in elements:
        #a.send_keys(Keys.PAGE_DOWN)
        time.sleep(0.2)
        try:
            p_name = a.find_element_by_class_name("ta-product-title").text.replace("\n","")
            
            p_info_text = ""
            
            p_img_url = a.find_element_by_class_name("lazy").get_attribute("src")
            
        except:
            
            return False
            
        productlist.append(productInList(p_name, p_img_url, p_info_text))  
    browser.close()
    
    productlist = toolz.unique(productlist, key=lambda x: x.product_name)

    json_string = json.dumps([ob.__dict__ for ob in productlist], ensure_ascii=False).encode('utf8')
    return json_string
    

