import { NextRequest, NextResponse } from "next/server";
import { auth } from "../auth/[...nextauth]/route";

export async function POST(request: NextRequest){
    try{
    const body = await request.json();
    const session = await auth();

    console.log(session?.accessToken)

    if (!session || !session.accessToken) {
        return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
    }

    const response = await fetch(`http://localhost:8087/orchestrator/processAccountCreation`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${session?.accessToken}`,
        },
        body: JSON.stringify(body),
    });
    
    const result = await response.json();

    if (!response.ok) {
      return new Response(result, { status: response.status });
    }
    

    return NextResponse.json(result, { status: response.status });
    
    } catch(error){
        console.error("API route error:", error);
        return NextResponse.json({ sucess: false, error: (error as Error).message }, { status: 500 });
    }
}