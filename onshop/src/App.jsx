import './App.css'
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import MainHeader from "./IndexPage/Pages/MainHeader.jsx";

const App = () => {
  return (
      <Router>
          <Routes>
              <Route path={"/"} element={<MainHeader />}>

              </Route>
          </Routes>
      </Router>
  )
}

export default App
