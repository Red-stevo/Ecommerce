import "./Styles/CategoricalProductsDisplay.css";
import CategoriesMenu from "./Components/CategoriesMenu.jsx";
import {Image} from "react-bootstrap";
import StarRating from "./Components/StarRating.jsx";



const products = [
    {productId:1,productName:"Unscented", productPrice:600, productRating:1, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.yuIhGQGVmD9pCQd22TKOWAHaHd%26pid%3DApi&f=1&ipt=3426d38adab2bfdeef0ee64ca0bd62888044ff3d5e318a419968579f1dbe6d63&ipo=images"},
    {productId:2,productName:"Cosmetics", productPrice:1700, productRating:3, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.f6brV2SJ0PX7OFpqNrcaCgHaFH%26pid%3DApi&f=1&ipt=333683c40fbe5857d4b2a053290b9764b0db8506d5682f23c26de523c8701675&ipo=images"},
    {productId:3,productName:"T-shirt", productPrice:600, productRating:5, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.ryvGFpkU99mutbVUNr16QgHaJ4%26pid%3DApi&f=1&ipt=b0fe13819fea50886ab035bdbb80259f9038e86c8b31ff378147fc4b6e82147c&ipo=images"},
    {productId:4,productName:"Shoes", productPrice:600, productRating:5, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.nsCvpZfngZbv1Dl5S2jx5gHaEK%26pid%3DApi&f=1&ipt=71915438ef3d4609e5f9a84a67e20dea2f8f782542c7c6514e02e54cc6635a20&ipo=images"},
    {productId:5,productName:"Bags", productPrice:600, productRating:4, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.73ClGYHGReevkne1_hVF7AAAAA%26pid%3DApi&f=1&ipt=1f4401a453523a3ba9b2c6ec357a8a0060bbc4590d9bce374c8da7b3a1afb49f&ipo=images"},
    {productId:6,productName:"Cooking Pots", productPrice:600, productRating:0, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.a5pmQDQiKe6ceaEXxi2cxwHaFf%26pid%3DApi&f=1&ipt=1580d8d2b5c8056bf4a7a65e2105213f7a4265bb804e3c3041176f3985fb30fd&ipo=images"},
    {productId:7,productName:"Television", productPrice:600, productRating:2, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.Ao8YcK6tWt8d8JVTA1J91AHaHa%26pid%3DApi&f=1&ipt=27fe040d5faee9f20ed5aa87f9a8297ef8a5792f47091aab0a88a56c82d3d2df&ipo=images"},
    {productId:8,productName:"Samsung Galaxy", productPrice:600, productRating:0, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.XwJ889hbrMie6iA452AivgHaE7%26pid%3DApi&f=1&ipt=7f4a257c12606d9d929fe28b513b9b39205cd4a85ff5b0ef6fe484e6098a7fea&ipo=images"},
    {productId:9,productName:"Chair", productPrice:600, productRating:3, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.fZQLpFVERxWeMK_tRSp_bAHaH6%26pid%3DApi&f=1&ipt=33290b2e3703725bec224d0400640af7f092e48f056e126f5a244baea6551125&ipo=images"},
    {productId:10,productName:"Chalks", productPrice:600, productRating:3, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.IZFCB2q5NNhCZ1c5V5uVEAHaHa%26pid%3DApi&f=1&ipt=54446e68b78e29e00e151beb60d06bca7b3d7e596c9d6e27ff2fd5039b58fd70&ipo=images"},
    {productId:1,productName:"Unscented", productPrice:600, productRating:5, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.yuIhGQGVmD9pCQd22TKOWAHaHd%26pid%3DApi&f=1&ipt=3426d38adab2bfdeef0ee64ca0bd62888044ff3d5e318a419968579f1dbe6d63&ipo=images"},
    {productId:2,productName:"Cosmetics", productPrice:1700, productRating:3, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.f6brV2SJ0PX7OFpqNrcaCgHaFH%26pid%3DApi&f=1&ipt=333683c40fbe5857d4b2a053290b9764b0db8506d5682f23c26de523c8701675&ipo=images"},
    {productId:3,productName:"T-shirt", productPrice:600, productRating:0, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.ryvGFpkU99mutbVUNr16QgHaJ4%26pid%3DApi&f=1&ipt=b0fe13819fea50886ab035bdbb80259f9038e86c8b31ff378147fc4b6e82147c&ipo=images"},
    {productId:4,productName:"Shoes", productPrice:600, productRating:0, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.nsCvpZfngZbv1Dl5S2jx5gHaEK%26pid%3DApi&f=1&ipt=71915438ef3d4609e5f9a84a67e20dea2f8f782542c7c6514e02e54cc6635a20&ipo=images"},
    {productId:5,productName:"Bags", productPrice:600, productRating:0, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.73ClGYHGReevkne1_hVF7AAAAA%26pid%3DApi&f=1&ipt=1f4401a453523a3ba9b2c6ec357a8a0060bbc4590d9bce374c8da7b3a1afb49f&ipo=images"},
    {productId:6,productName:"Cooking Pots", productPrice:600, productRating:4, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.a5pmQDQiKe6ceaEXxi2cxwHaFf%26pid%3DApi&f=1&ipt=1580d8d2b5c8056bf4a7a65e2105213f7a4265bb804e3c3041176f3985fb30fd&ipo=images"},
    {productId:7,productName:"Television", productPrice:600, productRating:3, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.Ao8YcK6tWt8d8JVTA1J91AHaHa%26pid%3DApi&f=1&ipt=27fe040d5faee9f20ed5aa87f9a8297ef8a5792f47091aab0a88a56c82d3d2df&ipo=images"},
    {productId:8,productName:"Samsung Galaxy", productPrice:600, productRating:2, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.XwJ889hbrMie6iA452AivgHaE7%26pid%3DApi&f=1&ipt=7f4a257c12606d9d929fe28b513b9b39205cd4a85ff5b0ef6fe484e6098a7fea&ipo=images"},
    {productId:9,productName:"Chair", productPrice:600, productRating:2, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.fZQLpFVERxWeMK_tRSp_bAHaH6%26pid%3DApi&f=1&ipt=33290b2e3703725bec224d0400640af7f092e48f056e126f5a244baea6551125&ipo=images"},
    {productId:10,productName:"Chalks", productPrice:600, productRating:3, productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.IZFCB2q5NNhCZ1c5V5uVEAHaHa%26pid%3DApi&f=1&ipt=54446e68b78e29e00e151beb60d06bca7b3d7e596c9d6e27ff2fd5039b58fd70&ipo=images"},
];


const CategoricalProductsDisplay = () => {
    return (
        <div className={"product-menu-holder"}>
            <CategoriesMenu />
            <div className={"product-display-section"}>
                {products.map(
                ({productId, productName,
                     productPrice,productRating,
                     productUrl}, index) => (
                    <div className={"product-gen-display"} key={index}>
                        <Image src={productUrl} alt={productName} className={"product-display-image"} />
                        <div>
                            <span className={"product-display-name"}>{productName}</span>
                            <StarRating active={true} value={productRating}/>
                            <span className={"product-display-price"}>ksh {productPrice}</span>
                        </div>

                    </div>
                ))}
            </div>
        </div>
    );
};

export default CategoricalProductsDisplay;