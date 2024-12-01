import ReactDOM from "react-dom/client";
import App from "./App";
import { CookiesProvider, useCookies } from "react-cookie";
import axios from "axios";

const rootElement = document.getElementById("root")!;
const root = ReactDOM.createRoot(rootElement);

root.render(
  <CookiesProvider>
    <App />
  </CookiesProvider>
);
