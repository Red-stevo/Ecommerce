import "./Styles/ProductCategories.css";
import {Image} from "react-bootstrap";
import {GrFormEdit} from "react-icons/gr";
import {GoPlus} from "react-icons/go";
import {useEffect, useState} from "react";
import AddIconForm from "./Components/AddIconForm.jsx";
import {useDispatch, useSelector} from "react-redux";
import {getCategories
} from "../../../ApplicationStateManagement/CategoriesStore/CategoriesReducer.js";
import Loader from "../../../Loading/Loader.jsx";


const ProductCategories = () => {
    const [modalShow, setModalShow] = useState(false);
    const [editData, setEditData] = useState(null);
    const dispatch = useDispatch();
    const {errorMessage, loading, success, categories} = useSelector(state => state.CategoriesReducer);

    const handleEditCategory = (categoryName,categoryIcon, categoryId) => {
           setEditData({categoryName, categoryIcon, categoryId,setModalShow});
           setModalShow(prevState => !prevState);
    }

    const  handleAddCategory = () => {
        setEditData(null);
        setModalShow(prevState => !prevState);
    }

    /*Load categories*/
    useEffect(() => {
        dispatch(getCategories())
    }, []);



    /*Fetch the products again*/
    useEffect(() => {
        if (success && !loading && !errorMessage) dispatch(getCategories());
    }, [success]);


    return (
        <div className={"product-categories-page"}>
            <span className={"product-categories-header"}>Product Categories</span>

            <section className={"product-categories-body"}>
                <div className={"product-categories-body-header"}>
                    <span className={"product-categories-body-header-icon"}>Icon</span>
                    <span className={"product-categories-body-header-category"}>Category</span>
                    <span></span>
                </div>

                <div className={"product-categories-body-contents"} >
                    {categories && categories.length > 0 && categories.map(({categoryIcon, categoryId, categoryName}) => (
                        <div key={categoryId} className={"icon-content"}>
                            <Image className={"category-icon-image"} src={categoryIcon} />
                            <span className={"category-name-display"}>{categoryName}</span>
                            <GrFormEdit className={"edit-category-icon"}
                                        onClick = {() => handleEditCategory(categoryName,categoryIcon, categoryId)} />
                        </div>
                    ))}

                    <span className={"product-categories-add-category"} onClick={() => setModalShow(true)} >
                        <GoPlus className={"plus-category"} />
                    </span>
                </div>

            </section>

            <AddIconForm editdata={editData} show={modalShow}
                         onHide={handleAddCategory}/>
            {loading && <Loader />}
        </div>
    );
};

export default ProductCategories;