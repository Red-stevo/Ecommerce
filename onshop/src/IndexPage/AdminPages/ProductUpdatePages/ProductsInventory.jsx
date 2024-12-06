import "./Styles/ProductsInventory.css";
import {Button, InputGroup} from "react-bootstrap";

const  InventoryResponse  = [
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:0,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
]

const categories = [
    {categoryName:"Fashion", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120001"},
    {categoryName:"books", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120002"},
    {categoryName:"home-appliances", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120003"},
    {categoryName:"toys", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120004"},
    {categoryName:"sports", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120005"},
    {categoryName:"groceries", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120006"},
    {categoryName:"furniture", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120007"},
    {categoryName:"automobiles", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120008"},
    {categoryName:"health", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac120009"},
    {categoryName:"beauty", categoryId:"4211f3dd-a7f6-11ef-ad26-0242ac1200010"},
]


const ProductsInventory = () => {
    return (
        <div className={"inventory-page"}>
            <div className={"inventory-page-section-a"}>
                <Button className={"app-button add-product-button"}>Add Product</Button>
            </div>

            <section className={"inventory-page-filters-section"}>

                <div className={"filter-holder"}>
                    <span>Category</span>
                    <select className={"form-select"}>
                        <option>All Products</option>
                        {categories.length > 0 && categories.map(({categoryId, categoryName}) => (
                            <option key={categoryId} value={categoryName}>{categoryName}</option>))
                        }
                    </select>
                </div>

                <div className={"filter-holder"}>
                    <span>Status</span>
                    <select className={"form-select"}>
                        <option>All Products</option>
                        <option>Active</option>
                        <option>Inactive</option>
                    </select>
                </div>

                <div className={"filter-holder"}>
                    <span>Price</span>
                    <InputGroup className="">
                        <input className={"productPrice form-control"} required={true}
                               aria-label="Product Price" placeholder={'Start Price'}
                               id={"productPrice"}/>
                        <input className={"productDiscount form-control"} required={true}
                               aria-label="Product discount" placeholder={"End discount"}
                               id={"discount"}/>
                    </InputGroup>
                </div>


            </section>
        </div>
    );
};

export default ProductsInventory;