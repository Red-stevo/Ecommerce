import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getCategories} from "../../../ApplicationStateManagement/CategoriesStore/CategoriesReducer.js";
import "./Styles/ProductUpdatePage.css";
import {Button, FloatingLabel} from "react-bootstrap";
import {IoIosClose} from "react-icons/io";
import FileUploadPreview from "./Components/FileUploadPreview.jsx";
import {getUpdateProducts
} from "../../../ApplicationStateManagement/ProductUpdateStore/ProductUpdateReducer.js";
import {useParams} from "react-router-dom";
import {useForm} from "react-hook-form";
import Loader from "../../../Loading/Loader.jsx";

function FileChange(setUploads, setPreviewFile) {
    return (event) => {
        const files = Array.from(event.target.files);

        {
            files.length > 0 && files.forEach((image) => {
                if (!image) return;

                /*Check the file size, if too large ignore the file.*/
                if (image.size > 10485760) {
                    console.log("The File '", image.name, "' is too large");
                    return;
                }

                /*add file to the upload list.*/
                setUploads((prevState) => [...prevState, image]);

                const reader = new FileReader();

                // Check if the file is an image or video
                if (image.type.startsWith("image/")) {
                    reader.onload = (e) => {
                        setPreviewFile((prevState) => [...prevState, {file: e.target.result, type: "image"}]);
                    };
                    reader.readAsDataURL(image);

                } else if (image.type.startsWith("video/") || image.type.startsWith("application/")) {
                    reader.onload = (e) => {
                        setPreviewFile((prevState) => [...prevState, {file: e.target.result, type: "video"}]);
                    };
                    reader.readAsDataURL(image);
                } else {
                    console.log(`File starts with ${image.type}`);
                    console.error("File type not supported. Please upload an image or video.");
                }
            })
        }
    };
}

const ProductUpdatePage = () => {
    const {categories} = useSelector(state => state.CategoriesReducer);
    const {product, errorMessage, loading, success} = useSelector(state=> state.ProductUpdateReducer);
    const {productName, productDescription, productCategory, specificProducts} = product;
    const dispatch = useDispatch();
    const [previewFile, setPreviewFile] = useState([]);
    const [uploads, setUploads] = useState([]);
    const {productid} = useParams();
    const [displayProduct, setDisplayProduct] = useState(0);
    const {register, reset,
        handleSubmit} = useForm();
    const [active, setActive] = useState(0);
    const [suggestions, setSuggestions] = useState([]);
    const [categoryInput, setCategoryInput] = useState("");
    const [productSelectedCategories, setProductSelectedCategories] = useState([]);


    useEffect(() => {
        dispatch(getCategories());
        dispatch(getUpdateProducts(productid));
    }, []);


    useEffect(() => {
        if (specificProducts || success) {
            const  {id, productPrice, productColor,
                discount, productSize, productImages, productCount:count} = specificProducts[displayProduct];
            setPreviewFile([...productImages])
            reset({productId:id, productName, productDescription,
                productPrice, discount, productColor, productSize, count});
            setProductSelectedCategories([...productCategory]);
        }
    }, [displayProduct, success]);


    const handleImageRemove = (imageIndex) => {

        if (typeof imageIndex !== "string") {
            setUploads(uploads.filter((_, index) => index !== imageIndex));
            setPreviewFile(previewFile.filter((_, index) => index !== imageIndex));
        }else {
            /*Delete product image.*/
            //1. handle preview.
            setPreviewFile(previewFile.filter(url => url !== imageIndex));

            //2. handle the database deletion.

            //3. handle the redux removal
        }
    }

    const handleFileChange = FileChange(setUploads, setPreviewFile);


    useEffect(() => {
        const handleCategoryChange = (inputData) => {
            if (inputData){
                const categorySuggestions = categories && categories
                    .filter(category  => category.categoryName.toLowerCase().includes(inputData.toLowerCase()));
                setSuggestions(categorySuggestions && categorySuggestions.length > 0 ? categorySuggestions : []);
            }
        }

        if (categoryInput) handleCategoryChange(categoryInput);
        else  setSuggestions([]);
    }, [categoryInput])


    const handleCategoryRemove = (category)  => {
        setProductSelectedCategories(prevState => prevState.filter(name => name !== category))
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
                    {productSelectedCategories && productSelectedCategories.length > 0 &&
                        productSelectedCategories.map((category, index) => (
                            <div key={index} className={"categories-preview"}>
                                {category}
                                <IoIosClose className={"cancel-categories"}
                                onClick={() => handleCategoryRemove(category)}/>
                            </div>))}
                </div>

                <input className={"input-category-select"} type={"text"} placeholder={"Product Category"}
                onChange={(event) => setCategoryInput(event.target.value)}/>

                {suggestions.length > 0 &&
                <div className={"product-update-suggestions"}>
                    {suggestions && suggestions.length > 0 && suggestions.map(({categoryName}, index) => (
                        <span className={"category-options"} key={index} onClick={() => {
                            setProductSelectedCategories(prevState => [...prevState, categoryName]);
                            setCategoryInput("");
                        }}>
                            {categoryName}
                        </span>
                    ))}
                </div>}

            </section>

            <div className={"product-update-page-proportion-variety-update"}>
                <input className={"product-update-page-variety-update"}
                       placeholder={"variety"} {...register("productColor")} />
                <input className={"product-update-page-proportion-update"}
                       placeholder={"proportion"} {...register("productSize")} />
            </div>

            <div className={"product-price-discount-update-input"}>
                <input className={"product-update-page-price-update"} placeholder={"price"}
                       {...register("productPrice")} />
                <input className={"product-update-page-discount-update"} placeholder={"discount"}
                       {...register("discount")} />
            </div>

            <div className={"product-update-page-count-triangle"}>
                <input type={"number"} className={"product-update-page-count-update"}
                       placeholder={"count"} {...register("count")} />

                <button className={"product-update-page-active-button app-button"}
                onClick={() => {
                    if (active === 1) setActive(2);
                    else setActive(1);
                }}>
                    {active === 1 ? "Active" : "Inactive"}
                </button>
            </div>

            <FileUploadPreview onChange={handleFileChange} previewImages={previewFile}
                               handleRemove={handleImageRemove}/>


            <div className={"action-buttons"}>
                <Button className={"app-button"}
                onClick={() => {
                    if (specificProducts && displayProduct === 0)
                        setDisplayProduct(specificProducts.length - 1);
                    else setDisplayProduct((prevState) => prevState - 1);
                }}>Previous</Button>
                <Button className={"app-button"}>Update</Button>
                <Button className={"app-button"}
                        onClick={() => {
                                if (specificProducts && displayProduct === (specificProducts.length - 1))
                                    setDisplayProduct(0);
                                else setDisplayProduct((prevState) => prevState + 1);
                        }}>Next</Button>
            </div>
            {loading && <Loader />}
        </div>
    );
};

export default ProductUpdatePage;