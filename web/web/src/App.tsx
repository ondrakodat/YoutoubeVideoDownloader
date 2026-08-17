import {useState} from "react"

function App() 
{
  const[url, setUrl] = useState("");
  const[type, setType] = useState("MP4");

    const downloadVideo = async () => {
        console.log("Posílám request na Spring...");

        try {
            const response = await fetch(
                `http://localhost:8080/api/download?url=${encodeURIComponent(url)}&type=${type}`,
                {
                    method: "POST"
                }
            );

            console.log("Spring odpověděl:", response.status);

            const data = await response.json();

            console.log("Data ze Springu:", data);

        } catch (error) {
            console.error("Chyba:", error);
        }
    };
  

  return (
    <div>
      <h1>Download Url Video</h1>
      <input type="text"
      placeholder = "Url videa"
      value = {url} onChange={
        (e) => setUrl(e.target.value)
      }
      />

      <select value={type}
          onChange={(e) => setType(e.target.value) }
      >

        <option value="MP4">MP4</option>
                <option value="MP3">MP3</option>
                <option value="MP4_SUBTITLES">MP4 + subtitles</option>
                <option value="MP4_SUBTITLES_AUTO">
                    MP4 + automatic subtitles
                </option>
      </select>

      <button onClick={downloadVideo}>
        Download
      </button>
    </div>
  );
}

export default App