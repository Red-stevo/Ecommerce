import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getCategories} from "../../../ApplicationStateManagement/CategoriesStore/CategoriesReducer.js";
import "./Styles/ProductUpdatePage.css";
import {FloatingLabel} from "react-bootstrap";
import {IoIosClose} from "react-icons/io";
import FileUploadPreview from "./Components/FileUploadPreview.jsx";
import {getUpdateProducts
} from "../../../ApplicationStateManagement/ProductUpdateStore/ProductUpdateReducer.js";
import {useParams} from "react-router-dom";
import {useForm} from "react-hook-form";

const ProductUpdatePage = () => {
    const {categories} = useSelector(state => state.CategoriesReducer);
    const {product, errorMessage, loading} = useSelector(state=> state.ProductUpdateReducer);
    const {productId, productName, productDescription, productCategory, specificProducts} = product;
    const dispatch = useDispatch();
    const [previewFile, setPreviewFile] = useState([]);
    const [uploads, setUploads] = useState([]);
    const [productDetailsList, setProductDetailsList] = useState([]);
    const [productCount, setProductCount] = useState(1);
    const {productid} = useParams();
    const {register, reset,
        handleSubmit} = useForm();


    useEffect(() => {
        dispatch(getCategories());
        dispatch(getUpdateProducts(productid));


    }, []);

    useEffect(() => {
        if (productCount <= 1) setProductCount(1);
    }, [productCount]);

    const handleImageRemove = (imageIndex) => {
        setUploads(uploads.filter((_,index) => index !== imageIndex));
        setPreviewFile(previewFile.filter((_, index) => index !== imageIndex))
    }
    const handleFileChange = FileChange(productDetailsList, setUploads, setPreviewFile);


    function FileChange(productDetailsList, setUploads, setPreviewFile) {
        return (event) => {
            const files = Array.from(event.target.files);

            {
                files.length > 0 && files.forEach((image) => {

                    if (!image) return;

                    /*Rename files*/
                    const file = new File([image], `${productDetailsList.length}+${image.name}`,
                        {
                            type: image.type,
                            lastModified: image.lastModified,
                        })

                    /*Check the file size, if too large ignore the file.*/
                    if (file.size > 10485760) {
                        console.log("The File '", file.name, "' is too large");
                        return;
                    }


                    /*add file to the upload list.*/
                    setUploads((prevState) => [...prevState, file]);

                    const reader = new FileReader();

                    // Check if the file is an image or video
                    if (file.type.startsWith("image/")) {
                        reader.onload = (e) => {
                            setPreviewFile((prevState) => [...prevState, {file: e.target.result, type: "image"}]);
                        };
                        reader.readAsDataURL(file);

                    } else if (file.type.startsWith("video/") || file.type.startsWith("application/")) {
                        reader.onload = (e) => {
                            setPreviewFile((prevState) => [...prevState, {file: e.target.result, type: "video"}]);
                        };
                        reader.readAsDataURL(file);
                    } else {
                        console.log(`File starts with ${file.type}`);
                        console.error("File type not supported. Please upload an image or video.");
                    }
                })
            }
        };
    }


    return (
        <div className={"product-update-page"}>

            <input className={"product-update-page-name-update"} type={"text"}
                   {...register("productName")} placeholder={"product Name"}/>

            <FloatingLabel className={"form-control product-update-page-description-update "} controlId="floatingTextarea"
                           label="Product Decription">
                <textarea required={true} className={"form-control description-update-input-field"}
                          {...register("productDescription")}
                          placeholder={"Product Description"} style={{height: '100px'}}/>
            </FloatingLabel>


            <section className={"product-update-page-categories"}>

                <div className={"selected-categories"}>
                    {productCategory && productCategory.length > 0 &&
                        productCategory.map((category, index) => (
                            <div key={index} className={"categories-preview"}>
                                {category}
                                <IoIosClose className={"cancel-categories"}/>
                            </div>))}
                </div>

                <input className={"input-category-select"} type={"text"} placeholder={"Product Category"}/>
            </section>

            <div className={"product-update-page-proportion-variety-update"}>
                <input className={"product-update-page-variety-update"} placeholder={"variety"}/>
                <input className={"product-update-page-proportion-update"} placeholder={"proportion"}/>
            </div>

            <div className={"product-price-discount-update-input"}>
                <input className={"product-update-page-price-update"} placeholder={"price"}/>
                <input className={"product-update-page-discount-update"} placeholder={"discount"}/>
            </div>

            <div className={"product-update-page-count-triangle"}>
                <input type={"number"} className={"product-update-page-count-update"}
                       value={productCount} placeholder={"count"}
                       onChange={(event) => setProductCount(event.target.value)}/>
            </div>

            <FileUploadPreview onChange={handleFileChange} previewImages={previewFile}
                               handleRemove={handleImageRemove}/>

        </div>
    );
};

export default ProductUpdatePage;