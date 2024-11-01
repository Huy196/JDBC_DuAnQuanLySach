package com.example.duanquanlysach.InterfaceShop;

import com.example.duanquanlysach.Product.Functoin_ProductCotroller;
import com.example.duanquanlysach.Product.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductDetialController {
    private Product product;
    @FXML
    private Label maProductDetail;
    @FXML
    private ImageView imageProductDetail;
    @FXML
    private Label nameProductDetail;
    @FXML
    private Label authorProductDetail;
    @FXML
    private Label publishingHouseProductDetail;
    @FXML
    private Label typeProductDetail;
    @FXML
    private Label yearProductDetail;
    @FXML
    private Label contentProductDetail;
    @FXML
    private Label priceProductDetail;



    public void setProductDetailData(Product product){
        Functoin_ProductCotroller productCotroller =new Functoin_ProductCotroller();
        this.product = product;

        maProductDetail.setText(Integer.toString(product.getMaSach()));
        imageProductDetail.setImage(new Image(product.getAnh()));
        nameProductDetail.setText(product.getTenSach());
        authorProductDetail.setText(product.getTacGia());
        contentProductDetail.setText(product.getNoiDung());
        yearProductDetail.setText(Integer.toString(product.getNamXB()));
        publishingHouseProductDetail.setText(productCotroller.getTenNXB(product.getMaNXB()));
        priceProductDetail.setText((product.getGiaSach()) + " ₫");
        typeProductDetail.setText(productCotroller.getTenLoaiSach(product.getMaLoaiSach()));
    }
}
