import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getCategories} from "../../../ApplicationStateManagement/CategoriesStore/CategoriesReducer.js";
import "./Styles/ProductUpdatePage.css";
import {FloatingLabel} from "react-bootstrap";
import {IoIosClose} from "react-icons/io";
import {GoTriangleDown, GoTriangleUp} from "react-icons/go";
import FileUploadPreview from "./Components/FileUploadPreview.jsx";

const ProductUpdatePage = () => {
    const {categories} = useSelector(state => state.CategoriesReducer);;
    const dispatch = useDispatch();
    const [previewFile, setPreviewFile] = useState([]);
    const productCategories = ["Shoes", "Toys", "Health Care"];
    const [uploads, setUploads] = useState([]);
    const [productDetailsList, setProductDetailsList] = useState([]);


    useEffect(() => {
        dispatch(getCategories());
    }, []);

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

            <input className={"product-update-page-name-update"} type={"text"}/>

            <FloatingLabel className={"form-control product-update-page-description-update "} controlId="floatingTextarea"
                           label="Product Decription">
                <textarea required={true} className={"form-control description-update-input-field"}
                          placeholder={"Product Description"} style={{height: '100px'}}/>
            </FloatingLabel>


            <section className={"product-update-page-categories"}>
                {productCategories && productCategories.length > 0 &&
                    productCategories.map((category, index) => (
                        <div key={index} className={"categories-preview"}>
                            {category}
                            <IoIosClose className={"cancel-categories"}/>
                        </div>
                    ))
                }
                <input className={"input-category-select"} type={"text"}/>
            </section>

            <div>
                <input className={"product-update-page-variety-update"} placeholder={"variety"}/>

                <input className={"product-update-page-proportion-update"} placeholder={"proportion"}/>
            </div>

            <div className={"proportion-variety-update-input"}>
                <input className={"product-update-page-price-update"} placeholder={"price"}/>

                <input className={"product-update-page-discount-update"} placeholder={"discount"}/>
            </div>

            <div className={"product-update-page-count-triangle"}>
                <GoTriangleUp className={"product-update-page-triangle"}/>
                <input className={"product-update-page-count-update"} placeholder={"count"}/>
                <GoTriangleDown className={"product-update-page-triangle"}/>
            </div>

            <FileUploadPreview onChange={handleFileChange} previewImages={previewFile}
                               handleRemove={handleImageRemove}/>

        </div>
    );
};

export default ProductUpdatePage;