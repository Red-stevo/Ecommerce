import "./Styles/AddProductPage.css";
import {Button, FloatingLabel, Form, InputGroup} from "react-bootstrap";
import {useEffect, useState} from "react";
import {IoIosClose} from "react-icons/io";
import {useForm} from "react-hook-form";
import {useDispatch, useSelector} from "react-redux";
import {postProduct} from "../../../ApplicationStateManagement/ProductStores/newProductReducer.js";
import Loader from "../../../Loading/Loader.jsx";
import {getCategories} from "../../../ApplicationStateManagement/CategoriesStore/CategoriesReducer.js";
import FileUploadPreview from "./Components/FileUploadPreview.jsx";


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

const AddProductPage = () => {
    const [uploads, setUploads] = useState([]);
    const [previewFile, setPreviewFile] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const {register, handleSubmit, reset
    } = useForm();
    const [productTopDetails, setProductTopDetails] = useState(null);
    const [productDetailsList, setProductDetailsList] = useState([]);
    const dispatch = useDispatch();
    const {loading,errorMessage} = useSelector(state => state.newProductReducer);
    const {categories} = useSelector(state => state.CategoriesReducer);


    const removeCategory=(category)=>{setSelectedCategories(selectedCategories.filter((item) => item !== category));}

    const handleImageRemove = (imageIndex) => {
       setUploads(uploads.filter((_,index) => index !== imageIndex));
       setPreviewFile(previewFile.filter((_, index) => index !== imageIndex))
    }
    const handleFileChange = FileChange(productDetailsList, setUploads, setPreviewFile);

    const handleAddProduct = (data) => {
        if (!productTopDetails){
            const {productDescription,productName } = data;
            setProductTopDetails({productName, productDescription, categoryName:selectedCategories});
        }

        const {color, size, productPrice, discount, count} = data;

        const productCreationRequest =
            {color, size, productPrice, discount, count, productUrls:uploads};

        setProductDetailsList((prevState) => [...prevState, productCreationRequest]);


        //reset start.
        setUploads([]);
        setPreviewFile([]);


        reset({color:'',size:'',productPrice:'',discount:'',count:''});
    }

    const handlePublish = () => {

        const  data = {...productTopDetails, productCreatedDetails:productDetailsList};

        reset({productName:'', productDescription:''});
        setProductTopDetails(null);
        setProductDetailsList([]);
        setSelectedCategories([]);


        /*Display save product action.*/
        dispatch(postProduct(data));
    }

    useEffect(() => {
        dispatch(getCategories());
    }, []);


    return (
        <>
            {loading && <Loader/>}
            <div className={"add-product-page"}>
                <div className={"title-form-buttons-holder"}>
                    <span className={"page-title"}>Add Product</span>
                    <Form className={"product-input-form"} onSubmit={handleSubmit(handleAddProduct)}>
                        <Form.Group>
                            <input className={"form-control"} required={true}
                                   placeholder={"Product Name"} id={"productName"}
                                   disabled={productTopDetails !== null}
                                   {...register("productName")} />
                        </Form.Group>

                        <FloatingLabel className={"product-description"} controlId="floatingTextarea"
                                       label="Product Decription">
                            <textarea
                                required={true}
                                className={"input-field form-control"}
                                placeholder={"Product Description"}
                                style={{height: '100px'}}
                                id={"productDescription"}
                                disabled={productTopDetails !== null}
                                {...register("productDescription")}
                            />

                        </FloatingLabel>

                        {/*Preview the selected categories.*/}
                        <div className={"categories-preview-holder"}>
                            {/*Pre view selected categories*/}
                            {selectedCategories.length > 0 && selectedCategories.map((category, index) => (
                                <div key={index} className={"categories-preview"}>
                                    {category}
                                    <IoIosClose className={"cancel-categories"}
                                                onClick={() => removeCategory(category)}/>
                                </div>))
                            }
                        </div>

                        <select className={"form-select"}
                                disabled={productTopDetails !== null}
                                onChange={(e) =>
                                    setSelectedCategories((prevState) => [...prevState, e.target.value])}>
                            <option>Select Product Categories</option>

                            {categories.length > 0 && categories.map(({categoryId, categoryName}) => (
                                <option key={categoryId} value={categoryName}>{categoryName}</option>))
                            }
                        </select>

                        <div className={"size-color"}>
                            <Form.Group>
                                <input className={"form-control"} required={true}
                                       placeholder={"Product Variety"} id={"color"} {...register("color")} />
                            </Form.Group>

                            <Form.Group>
                                <input className={"form-control"} placeholder={"Product Proportion"}
                                       id={"size"} {...register("size")} required={true}/>
                            </Form.Group>
                        </div>

                        <div className={"price-count"}>
                            <InputGroup className="">
                                <input className={"productPrice form-control"} required={true}
                                       aria-label="Product Price" placeholder={'Product Price'}
                                       id={"productPrice"} {...register("productPrice")} />
                                <input className={"productDiscount form-control"} required={true}
                                       aria-label="Product discount" placeholder={"Product discount"}
                                       id={"discount"} {...register("discount")} />
                            </InputGroup>

                            <Form.Group>
                                <input className={"form-control"} placeholder={"Product Count"} required={true}
                                       id={"count"} {...register("count")} />
                            </Form.Group>
                        </div>

                        <FileUploadPreview onChange={handleFileChange} previewImages={previewFile}
                                      handleRemove={handleImageRemove}/>


                        <div className={"submit-buttons"}>
                            <Button className={"app-button"} type={"submit"}>Save</Button>
                            <Button className={"app-button"}
                                    onClick={() => handlePublish()}
                                    disabled={!(productTopDetails && productDetailsList.length > 0)}>
                                Publish
                            </Button>
                        </div>
                    </Form>
                </div>
            </div>
        </>
    );
};

export default AddProductPage;