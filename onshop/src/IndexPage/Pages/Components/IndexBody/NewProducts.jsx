import {Button, Card, Image} from "react-bootstrap";

const products =
    [
        {
        sectionName:"Selected Only For You.", productsList:[
            {productName:"HP laptop HP laptop HP laptop", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.ed-29-ckMum--DpdlJRKHgHaFW%26pid%3DApi&f=1&ipt=6e5a9225c92f4445f0691c7f7115830cf6d22c677e97c161d6e1ccd401f19ca4&ipo=images", productPrice:30000},
            {productName:"Lenovo laptop", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images", productPrice:2900},
            {productName:"HP laptop", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.ed-29-ckMum--DpdlJRKHgHaFW%26pid%3DApi&f=1&ipt=6e5a9225c92f4445f0691c7f7115830cf6d22c677e97c161d6e1ccd401f19ca4&ipo=images", productPrice:27500},
            {productName:"HP laptop", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.ed-29-ckMum--DpdlJRKHgHaFW%26pid%3DApi&f=1&ipt=6e5a9225c92f4445f0691c7f7115830cf6d22c677e97c161d6e1ccd401f19ca4&ipo=images", productPrice:34500},
            {productName:"HP laptop", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.ed-29-ckMum--DpdlJRKHgHaFW%26pid%3DApi&f=1&ipt=6e5a9225c92f4445f0691c7f7115830cf6d22c677e97c161d6e1ccd401f19ca4&ipo=images", productPrice:25000},
            {productName:"Lenovo laptop", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images", productPrice:34000},
            {productName:"Lenovo laptop", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images", productPrice:24000},
            {productName:"Lenovo laptop", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images", productPrice:20000},
        ]
    },
    {
        sectionName:"Most Popular Products.", productsList:[
            {productName:"Electronics", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.aM-uPtUP_z2z1xAAPsxjHwHaE8%26pid%3DApi&f=1&ipt=3b0fcd71019f58c50fc62a46b757821f51cb2ac08191f97abb782d04b794d7d5&ipo=images", productPrice:500},
            {productName:"Television set", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.t6FQsL3ntKtlIvQK1VwhfgHaHa%26pid%3DApi&f=1&ipt=45fbb932dd8b43a6a4d31f3a2e49f949f78223d13b6fd841c88431f869901b99&ipo=images", productPrice:24000},
            {productName:"Oven", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.uSG0M2_j183v6ZPSXdj6UgHaE8%26pid%3DApi&f=1&ipt=9f7dd094399cc76b6ecfbea2e7b37b055c5cfa92b8f2b7c993c1f4063808e23c&ipo=images", productPrice:34500},
            {productName:"Gas Cooker", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.fZCnR2Kz4exKzaUwEK3MkgHaGk%26pid%3DApi&f=1&ipt=f86d7ff6e62d7a8e2c90fe2deb348dfe1a1d35811a8345edbb5529a90b902610&ipo=images", productPrice:3500},
            {productName:"Shoes", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.0BHfO1YvKZPns7lOjJUS_QHaE9%26pid%3DApi&f=1&ipt=faac819633b48737f7019be0059d48862d46f6d7793f5d0210f37ce6772fc666&ipo=images", productPrice:2500},
            {productName:"Electronics", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.aM-uPtUP_z2z1xAAPsxjHwHaE8%26pid%3DApi&f=1&ipt=3b0fcd71019f58c50fc62a46b757821f51cb2ac08191f97abb782d04b794d7d5&ipo=images", productPrice:700},
        ]
    },
    {
        sectionName:"New Products", productsList:[
            {productName:"Make-up kit", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.0-1WWvkfLcrJT5e0pXN69QHaGe%26pid%3DApi&f=1&ipt=1c03c2d262b60d080b2d7ae3642b040e736aea4495cac2ff1dd316d4912e0741&ipo=images", productPrice:4500},
            {productName:"Perfumes", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.GpGXz_grRDD0TombeKMvBgHaE8%26pid%3DApi&f=1&ipt=f4d4e94822007a9a0674173f102d00155e14c0ffdf966ff7df40c11c7c72d361&ipo=images", productPrice:100},
            {productName:"Furniture", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.7O7GLd6JKCp-DoRllGB2ngHaGL%26pid%3DApi&f=1&ipt=0dff2638752a82e532c49eb25071a34cb5dd9431131c85874d803830622219fa&ipo=images", productPrice:40000},
            {productName:"Foot Balls", productUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.u2x3ib2_zcC0pIgohF7mpAHaE8%26pid%3DApi&f=1&ipt=41d61801f0573bc1117e840c480944cb8ba660e3cb02bf834e88196cf3d9c98f&ipo=images", productPrice:2500},
        ]
    }]



const NewProducts = () => {
    return (
        <div>
            {products.map(({productsList, sectionName}, index) =>(
                <div key={index} className={"category-section-holder"}>
                    <span className={"section-heading"}>{sectionName}</span>
                    <div  className={"category-section"}>
                        {
                            productsList.map(({productName, productUrl, productPrice}, index) => (
                                <div key={index} className={"product-display"}>
                                    <Image src={productUrl} width={250} height={250} />
                                    <Card.Body>
                                        <Card.Title>{productName}</Card.Title>
                                        <span>KSH {productPrice}</span>
                                        <Button className={"app-button view-categories-products"}>View Product</Button>
                                    </Card.Body>
                                </div>
                            ))
                        }
                    </div>
                </div>))}
        </div>
    );
};

export default NewProducts;