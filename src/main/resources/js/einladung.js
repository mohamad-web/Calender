document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ Script geladen!");
    document.getElementById("terminErstellenBtn").addEventListener("click", erstelleTermin);
});

async function erstelleTermin() {
    console.log("🟢 Termin-Erstellen-Button wurde geklickt!");

    let ausgewählteBenutzer = Array.from(document.querySelectorAll(".user-checkbox:checked"))
        .map(checkbox => checkbox.value);

    console.log("👤 Eingeladene Benutzer:", ausgewählteBenutzer);

    if (ausgewählteBenutzer.length === 0) {
        alert("Bitte wählen Sie mindestens einen Benutzer aus!");
        return;
    }

    let terminData = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        startTime: document.getElementById("startTime").value,
        endTime: document.getElementById("endTime").value,
        eingeladeneBenutzer: ausgewählteBenutzer.join(",")
    };

    console.log("📅 Gesendete Termin-Daten:", terminData);

    try {
        let response = await fetch("/termine/neu", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(terminData)
        });

        console.log("🔄 Server-Antwort erhalten.");

        if (!response.ok) {
            throw new Error("Fehler beim Erstellen des Termins.");
        }

        alert("✅ Termin erfolgreich erstellt! Einladungen wurden gesendet.");
        window.location.href = "/dashboard";
    } catch (error) {
        console.error("🚨 Fehler beim Senden der Anfrage:", error);
        alert("Ein Fehler ist aufgetreten. Bitte prüfen Sie die Konsole.");
    }
}
