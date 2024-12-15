import {Image} from "react-bootstrap";
import {RxTriangleDown, RxTriangleUp} from "react-icons/rx";

const ProductDisplayComponent = ({productId, productImage, productName, productPrice, productCount}) => {
    return (
        <div key={productId} className={"payment-page-products-section-product"}>
            <Image src={productImage}
                   className={"payment-page-products-section-product-image"}/>

            <span className={"payment-page-products-section-product-name"}>{productName}</span>

            <div className={"payment-page-products-section-product-price-count"}>
                <span className={"payment-page-products-section-product-price"}>
                    ksh {productPrice}
                </span>
                <span className={"payment-page-products-section-product-count"}>
                    <span className={"quantity-toggle-section"}>
                        <RxTriangleUp/>
                        {productCount}
                        <RxTriangleDown/>
                    </span>
                </span>
            </div>

        </div>
    );
};

export default ProductDisplayComponent;