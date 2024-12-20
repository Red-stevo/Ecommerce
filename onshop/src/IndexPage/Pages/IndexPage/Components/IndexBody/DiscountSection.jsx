import {Carousel, Image} from "react-bootstrap";
import {MdOutlineKeyboardDoubleArrowLeft, MdOutlineKeyboardDoubleArrowRight} from "react-icons/md";
import DiscountDisplay from "../DiscountDisplay.jsx";
import onOfferImage from "../../../../../assets/onOffer.png";
import {FaTag} from "react-icons/fa";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {
    getDiscountedProduct
} from "../../../../../ApplicationStateManagement/IndexPageStore/DiscountedProductsReducer.js";
import Loader from "../../../../../Loading/Loader.jsx";

const discountProducts = [
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"shoes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.xiLk6II7SS7bLC-CXwSykwHaE8%26pid%3DApi&f=1&ipt=0f2c2753596018612cd1d52c05d849552a572a04e6ac2ec169ec8abe916f6879&ipo=images", 'oldPrice':210, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"shoes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.YOcT7qTW2UTLkp4zSrbsfwHaHa%26pid%3DApi&f=1&ipt=857bdb82e0b2845541ec3fba6929c4354b1db5dcc6a00d049f6fdf865a5f5a5e&ipo=images", 'oldPrice':200, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"shoes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.M_yPW_pPIxDHh1XbVg2MRAHaJQ%26pid%3DApi&f=1&ipt=6192a1862b7466a5a9a8e14ca92a8bfbec445a72a1acb85cc1c456f81d85c512&ipo=images", 'oldPrice':230, 'newPrice':150, color:"whitesmoke"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"shoes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.1FCSQVl1N0P3N-etVzhNNQHaEK%26pid%3DApi&f=1&ipt=e0cd4678d6a58254cecd7eb8448248833194d6b6f34657dba399bc8e724a7173&ipo=images", 'oldPrice':250, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"clothes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.FViqANwHlfUDGdA5Wtm6fgHaId%26pid%3DApi&f=1&ipt=dcf3444259e3f6ecea0e8a56a7ea32e7a167b44f03c5b41b1d6d457c1f39b169&ipo=images", 'oldPrice':300, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"clothes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.obi3X73hahF9hBFIWf53nAHaJM%26pid%3DApi&f=1&ipt=c2bb998d9e43c6401302cc13c6956313e77f88033d3f7f0222f7d8a80efd9972&ipo=images", 'oldPrice':410, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"clothes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.iclRflFrvReW6WLdsURKggHaJB%26pid%3DApi&f=1&ipt=26fbe8fd75329f4c21f69ca0ab9a9f753788063113d7137f3e354770e0d9777f&ipo=images", 'oldPrice':540, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"clothes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.YbPcrg7IBD7m_6Nn2CTfugHaHa%26pid%3DApi&f=1&ipt=038fc1849f0239169d38457fc2c89ad5dc473be4703bfb04683c71eafefd28f4&ipo=images", 'oldPrice':220, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"utensils",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.explicit.bing.net%2Fth%3Fid%3DOIP.-vyyxaw9V-6j89JfJmIgzAHaHJ%26pid%3DApi&f=1&ipt=79a77b4d535f0fe732113109cc19d3daf3842f41350b3cd401a6c0fc8d6f24af&ipo=images", 'oldPrice':200, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"utensils",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.g6iN7mCJML6d6C2yttSMxQHaEK%26pid%3DApi&f=1&ipt=b387eaa931ce6d0f759ae8cc27dc3b3f1bd59000fd8826a479b5a6744a5afa02&ipo=images", 'oldPrice':320, 'newPrice':150, color:"white"},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"utensils",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.kp4w3vcLWqTUTH_t0ar-xgHaFM%26pid%3DApi&f=1&ipt=1f9f337253e5a6e745da5ee308372ba37fa08d410371d8a390d4865abf4ad2d0&ipo=images", 'oldPrice':270, 'newPrice':150, color:"white"},
]
const DiscountSection = () => {
    const dispatch = useDispatch();
    const {discountProducts,
        loading, errorMessage} = useSelector(status =>status.DiscountedProductsReducer);


    useEffect(() => {
        dispatch(getDiscountedProduct());
    }, []);

    return (
        <div className={"discount-products"}>

            {discountProducts &&
            <Carousel id={"image-holder"} slide={true} className={"discount-products-carousel"}
            prevIcon={<MdOutlineKeyboardDoubleArrowLeft className={"discount-icon-prev"} />}
            nextIcon={<MdOutlineKeyboardDoubleArrowRight className={"discount-icon-next"} />}>
                {discountProducts.map((product, index) => (
                    <Carousel.Item interval={1500} key={index} className={"discount-product"} >
                        <Image src={product.productImagesUrl} alt={product.productName} height={250}
                               width={250} className={"carousel-image"} />
                        <Carousel.Caption className={"caption-text"}>
                            <div className={"discount-product-text"}>
                                <span className={"discount-product-text-product-name"}>{product.productName}</span>
                            </div>
                            <div className={"star-burst-positioning"}>
                                <DiscountDisplay newPrice={product.discountedPrice}
                                                 oldPrice={product.discountedPrice + product.discount} />
                            </div>
                            <span className={"small-screen-offer-display"}>
                                {Math.round((((product.discountedPrice + product.discount) - product.discountedPrice)/(product.discountedPrice + product.discount)) * 100)}% OFF</span>
                        </Carousel.Caption>
                    </Carousel.Item>
                ))}
            </Carousel>}

            {loading && <Loader />}
        </div>
    );
};

export default DiscountSection;