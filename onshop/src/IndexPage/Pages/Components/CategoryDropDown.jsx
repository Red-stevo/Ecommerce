import {Dropdown} from "react-bootstrap";

const CategoryDropDown = () => {
    const categoryList = ["Men","Woman","Shoes", "Clothes", "Phones","Bags"];

    return (
        <Dropdown className={"category-dropdown"}>
            <Dropdown.Toggle className={"search-buttons-left"} id="dropdown-button-dark-example1" variant="secondary">
                Category
            </Dropdown.Toggle>
            <Dropdown.Menu>
                {categoryList.map(((item, index) => (
                    <Dropdown.Item key={index} href="#/action-1" >{item}</Dropdown.Item>
                )))}
            </Dropdown.Menu>
        </Dropdown>
    );
};

export default CategoryDropDown;