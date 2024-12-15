import {Dropdown} from "react-bootstrap";
import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import {getCategories} from "../../../../../ApplicationStateManagement/CategoriesStore/CategoriesReducer.js";

const CategoryDropDown = ({handleSearchProduct}) => {
    const dispatch = useDispatch();
    const {errorMessage,categories} = useSelector(state => state.CategoriesReducer);


    /*Load categories*/
    useEffect(() => {
        dispatch(getCategories())
    }, []);


    return (
        <Dropdown className={"category-dropdown"}>
            <Dropdown.Toggle className={"search-buttons-left"} id="dropdown-button-dark-example1" variant="secondary">
                Category
            </Dropdown.Toggle>
            <Dropdown.Menu>
                <Dropdown.Item>All Categories</Dropdown.Item>
                {categories && categories.map((({categoryId, categoryName}, index) => (
                    <Dropdown.Item key={categoryId} onClick={() => handleSearchProduct(categoryName)}>
                        {categoryName}
                    </Dropdown.Item>
                )))}
            </Dropdown.Menu>
        </Dropdown>
    );
};

export default CategoryDropDown;