import {Button, Carousel, Image} from "react-bootstrap";
import {TiStarburst} from "react-icons/ti";
import {MdOutlineKeyboardDoubleArrowLeft, MdOutlineKeyboardDoubleArrowRight} from "react-icons/md";

const discountProducts = [
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"shoes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.xiLk6II7SS7bLC-CXwSykwHaE8%26pid%3DApi&f=1&ipt=0f2c2753596018612cd1d52c05d849552a572a04e6ac2ec169ec8abe916f6879&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"shoes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.YOcT7qTW2UTLkp4zSrbsfwHaHa%26pid%3DApi&f=1&ipt=857bdb82e0b2845541ec3fba6929c4354b1db5dcc6a00d049f6fdf865a5f5a5e&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"shoes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.M_yPW_pPIxDHh1XbVg2MRAHaJQ%26pid%3DApi&f=1&ipt=6192a1862b7466a5a9a8e14ca92a8bfbec445a72a1acb85cc1c456f81d85c512&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"shoes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.1FCSQVl1N0P3N-etVzhNNQHaEK%26pid%3DApi&f=1&ipt=e0cd4678d6a58254cecd7eb8448248833194d6b6f34657dba399bc8e724a7173&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"clothes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.FViqANwHlfUDGdA5Wtm6fgHaId%26pid%3DApi&f=1&ipt=dcf3444259e3f6ecea0e8a56a7ea32e7a167b44f03c5b41b1d6d457c1f39b169&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"clothes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.obi3X73hahF9hBFIWf53nAHaJM%26pid%3DApi&f=1&ipt=c2bb998d9e43c6401302cc13c6956313e77f88033d3f7f0222f7d8a80efd9972&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"clothes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.iclRflFrvReW6WLdsURKggHaJB%26pid%3DApi&f=1&ipt=26fbe8fd75329f4c21f69ca0ab9a9f753788063113d7137f3e354770e0d9777f&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"clothes",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.YbPcrg7IBD7m_6Nn2CTfugHaHa%26pid%3DApi&f=1&ipt=038fc1849f0239169d38457fc2c89ad5dc473be4703bfb04683c71eafefd28f4&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"utensils",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.explicit.bing.net%2Fth%3Fid%3DOIP.-vyyxaw9V-6j89JfJmIgzAHaHJ%26pid%3DApi&f=1&ipt=79a77b4d535f0fe732113109cc19d3daf3842f41350b3cd401a6c0fc8d6f24af&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"utensils",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.g6iN7mCJML6d6C2yttSMxQHaEK%26pid%3DApi&f=1&ipt=b387eaa931ce6d0f759ae8cc27dc3b3f1bd59000fd8826a479b5a6744a5afa02&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"utensils",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.kp4w3vcLWqTUTH_t0ar-xgHaFM%26pid%3DApi&f=1&ipt=1f9f337253e5a6e745da5ee308372ba37fa08d410371d8a390d4865abf4ad2d0&ipo=images", 'oldPrice':200, 'newPrice':150},
            {'productDescription':"Elevate your style with [Product Name]. Durable, stylish, and perfect for any occasion.", 'productName':"utensils",'url':"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIF.LqfA2sL%252b1WdtLMFHUC7HDQ%26pid%3DApi&f=1&ipt=ee61ab2b488785a502dcb8bcb05e2467a6843cb3c024c3d8599c1d9703a5140f&ipo=images", 'oldPrice':200, 'newPrice':150}
]
const DiscountSection = () => {
    return (
        <div className={"discount-products"}>
            <Carousel id={"image-holder"} slide={true} className={"discount-products-carousel"}
            prevIcon={<MdOutlineKeyboardDoubleArrowLeft className={"discount-icon-prev"} />}
            nextIcon={<MdOutlineKeyboardDoubleArrowRight className={"discount-icon-next"} />}>
                {discountProducts.map((product, index) => (
                    <Carousel.Item interval={1500} key={index} className={"discount-product"}>
                        <Image src={product.url} alt={product.productName} height={200}
                               width={200} className={"carousel-image"} />
                        <Carousel.Caption className={"caption-text"}>
                            <TiStarburst className={"discount-starburst"} />
                            <div className={"discount-product-text"}>
                                <span className={"discount-product-text-product-name"}>{product.productName}</span>
                                <span className={"discount-product-text-description"}>{product.productDescription}</span>
                            </div>
                                <Button className={" app-button discount-product-view-product-button"}>
                                    View Product
                                </Button>
                            <div className={"prices-discounted-products"}>
                                <span className={"new-discounted-price"}>Now</span>
                                <span className={"new-discounted-price"}>KSH {product.newPrice} Only</span>
                                <span className={"old-product-price"}>KSH {product.oldPrice}</span>
                            </div>
                        </Carousel.Caption>
                    </Carousel.Item>
                ))}
            </Carousel>
        </div>
    );
};

export default DiscountSection;