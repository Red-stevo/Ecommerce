import { FaStar } from "react-icons/fa";
import { useState } from "react";

const StarRating = ({active, value}) => {
    const [rating, setRating] = useState(value);

    return (
        <div style={{ display: "flex", cursor: "pointer" }}>
            {[...Array(5)].map((star, index) => {
                const currentRating = index + 1;
                return (
                    <label key={index} style={{ margin: "0 5px" }}>
                        <input type="radio" name="rate" id={`star-${currentRating}`} value={currentRating}
                            disabled={active}   style={{ display: "none" }} onClick={() => setRating(currentRating)} />
                        <FaStar color={currentRating <= rating ? "#FFD700" : "grey"} className={"star-rating"} />
                    </label>
                );
            })}
        </div>
    );
};

export default StarRating;
