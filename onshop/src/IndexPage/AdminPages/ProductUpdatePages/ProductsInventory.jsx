import "./Styles/ProductsInventory.css";
import {Button, Image, InputGroup} from "react-bootstrap";
import {CiEdit} from "react-icons/ci";
import { FiTrash2 } from "react-icons/fi";
import ReactPaginate from "react-paginate";
import {PiArrowFatLeftThin, PiArrowFatRightThin} from "react-icons/pi";
import {useDispatch} from "react-redux";
import {useEffect} from "react";
import {fetchInventory} from "../../../ApplicationStateManagement/InventoryStore/InventoryReducer.js";
import {deleteProduct} from "../../../ApplicationStateManagement/ProductStores/newProductReducer.js";

const  InventoryResponse  = [
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:1,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:1,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:1,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:1,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:1,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:1,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:1,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:1,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
    {productName:"Wireless Earbuds", unitPrice:3500, quantity:103, status:2,imageUrl:"https://i5.walmartimages.com/asr/c48aa8a1-95bf-4c40-b798-d55eac7eff39_1.098ecde4b7f02c72568e2e00ae8f9864.jpeg"},
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
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(fetchInventory());
    }, []);


    const handlePageClick = (event) => {

    };

    const handleDeleteProduct = (productId) => {dispatch(deleteProduct(productId));}

    return (
        <div className={"inventory-page"}>
            <div className={"inventory-page-section-a"}>
                <Button href={"/admin/newProduct"} className={"app-button add-product-button"}>Add Product</Button>
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


            <section className={"inventory-page-products-section"}>

                <div className={"inventory-page-products-section-header"}>
                    <span className={"Product-Name"}>Product Name</span>
                    <span className={"Unit-Price"}>Unit Price</span>
                    <span className={"Quantity"}>Quantity</span>
                    <span className={"Status"}>Status</span>
                    <span className={"Action"}>Action</span>
                </div>

                <div className={"inventory-page-products-section-products"}>
                    {InventoryResponse && InventoryResponse.length > 0 && InventoryResponse.map(
                    ({productName, unitPrice,quantity, imageUrl, status, productId}, index) => (
                        <div key={index}
                            className={`inventory-page-products-section-product 
                            ${status === 1 && " red-border "}
                            ${status === 2 && " green-border "}`} >

                            <div className={"inventory-product-image-name"}>
                                <Image src={imageUrl} className={"product-inventory-image"} />
                                <span className={"inventory-product-name"}>{productName}</span>
                            </div>

                            <span className={"inventory-product-unit-price"}>ksh {unitPrice}</span>
                            <span className={"inventory-product-quantity"}>{quantity}</span>

                            {status && status === 1 ?
                                <span className={"inventory-product-status"}>Active</span>:
                                status === 2 &&
                                <span className={"inventory-product-status"}>Inactive</span>
                            }

                            <div className={"edit-delete-inventory-buttons"}>
                                <Button className={"app-button edit-product-inventory"}><CiEdit /> Edit</Button>
                                <FiTrash2 className={"trash-product-inventory"}
                                          onClick={() => handleDeleteProduct(productId)}/>
                            </div>

                        </div>
                    ))}
                </div>

            </section>

            <div className={"pager-component-holder"}>
                <ReactPaginate
                    pageCount={10} onPageChange={handlePageClick} containerClassName="pagination"
                    activeClassName="active-page" previousClassName="prev-item" nextClassName="next-item"
                    pageRangeDisplayed={4} marginPagesDisplayed={0}
                    pageClassName="page-item"
                    previousLabel={<PiArrowFatLeftThin className={"pager-arrow"} />}
                    nextLabel={<PiArrowFatRightThin className={"pager-arrow"} />}/>
            </div>
        </div>
    );
};

export default ProductsInventory;